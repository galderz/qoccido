package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.Function2;

import javax.lang.model.element.Modifier;

record BinaryOperator(
    Expression<?> left
    , String operator
    , Expression<?> right
    , ParamType<?> returns
)
{
    static BinaryOperator of(Recipe.BinaryOperator recipe)
    {
        return new BinaryOperator(
            Literal.of(ParamType.of(((Recipe.Type) recipe.left()).type()))
            , recipe.operator()
            , Literal.of(ParamType.of(((Recipe.Type) recipe.right()).type()))
            , ParamType.booleanType()
        );
    }

    MethodSpec operatorMethodSpec()
    {
        return MethodSpec.methodBuilder(operatorMethodName())
            .addModifiers(Modifier.STATIC)
            .addParameter(left.returns().type(), "v1")
            .addParameter(left.returns().type(), "v2")
            .addStatement("return v1 $L v2", operator)
            // TODO use return type...
            .returns(boolean.class)
            .build();
    }

    MethodSpec forAllMethodSpec()
    {
        return MethodSpecs.toMethodSpec2(
            left
            , right
            , expected2()
            , actual2(operatorMethodName())
            , String.format("forAll_%s", operatorMethodName())
        );
    }

    private String operatorMethodName()
    {
        return String.format(
            "%s_%s_%s"
            , operatorLiteral()
            , left.id()
            , right.id()
        );
    }

    private <T1, T2> Function2<T1, T2, String> expected2()
    {
        final Function2<T1, T2, Object> invoke = this::invoke;
        return invoke.andThen(ret -> Unchecked.<ParamType<Object>>cast(returns).toLiteral().apply(ret));
    }

    private Function2<String, String, String> actual2(String operatorMethodName)
    {
        return (v1, v2) -> String.format(
            "%s(%s, %s)"
            , operatorMethodName
            , v1
            , v2
        );
    }

    private String operatorLiteral()
    {
        return switch (operator)
        {
            case "<" -> "lessThan";
            case ">" -> "moreThan";
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private boolean invoke(Object v1, Object v2)
    {
        return switch (operator)
        {
            case "<" -> lessThan(v1, v2);
            case ">" -> moreThan(v1, v2);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private boolean lessThan(Object v1, Object v2)
    {
        return invokeCompare(
            v1
            , v2
            , (d1, d2) -> d1 < d2
            , (f1, f2) -> f1 < f2
            , (l1, l2) -> l1 < l2
        );
    }

    private boolean moreThan(Object v1, Object v2)
    {
        return invokeCompare(
            v1
            , v2
            , (d1, d2) -> d1 > d2
            , (f1, f2) -> f1 > f2
            , (l1, l2) -> l1 > l2
        );
    }

    private boolean invokeCompare(
        Object v1
        , Object v2
        , Function2<Double, Double, Boolean> doubleFn
        , Function2<Float, Float, Boolean> floatFn
        , Function2<Long, Long, Boolean> longFn
    )
    {
        if (v1 instanceof Double d1)
        {
            if (v2 instanceof Double d2)
            {
                return doubleFn.apply(d1, d2);
            }
        }
        else if (v1 instanceof Float f1)
        {
            if (v2 instanceof Float f2)
            {
                return floatFn.apply(f1, f2);
            }
        }
        else if (v1 instanceof Long l1)
        {
            if (v2 instanceof Long l2)
            {
                return longFn.apply(l1, l2);
            }
        }

        throw new RuntimeException(String.format(
            "lessThan not handled for combination of v1 class %s and v2 class %s"
            , v1.getClass()
            , v2.getClass()
        ));
    }
}
