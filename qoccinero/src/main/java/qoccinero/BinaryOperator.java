package qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.Function2;

import javax.lang.model.element.Modifier;

record BinaryOperator(
    String operator
    , ParamType<?> param1
    , ParamType<?> param2
    , ParamType<?> returns
)
{
    static BinaryOperator of(BinaryOperatorRecipe recipe)
    {
        return new BinaryOperator(
            recipe.operator()
            , ParamType.of(recipe.type1())
            , ParamType.of(recipe.type2())
            , ParamType.booleanType()
        );
    }

    MethodSpec operatorMethodSpec()
    {
        return MethodSpec.methodBuilder(operatorMethodName())
            .addModifiers(Modifier.STATIC)
            .addParameter(param1.type(), "v1")
            .addParameter(param2.type(), "v2")
            .addStatement("return v1 $L v2", operator)
            // TODO use return type...
            .returns(boolean.class)
            .build();
    }

    MethodSpec forAllMethodSpec()
    {
        return MethodSpecs.toMethodSpec2(
            param1
            , param2
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
            , param1.type().getName().replace('.', '_')
            , param2.type().getName().replace('.', '_')
        );
    }

    private <T1, T2> Function2<T1, T2, String> expected2()
    {
        final Function2<T1, T2, Object> invoke = this::invoke;
        return invoke.andThen(ret -> Unchecked.<ParamType<Object>>cast(returns).toLiteral().apply(ret));
    }

    private <T1, T2> Function2<T1, T2, String> actual2(String operatorMethodName)
    {
        return (v1, v2) -> String.format(
            "%s(%s /* %s */, %s /* %s */)"
            , operatorMethodName
            , Unchecked.<ParamType<T1>>cast(param1).toLiteral().apply(v1)
            , Unchecked.<ParamType<T1>>cast(param1).toHex(v1)
            , Unchecked.<ParamType<T2>>cast(param2).toLiteral().apply(v2)
            , Unchecked.<ParamType<T2>>cast(param2).toHex(v2)
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

    private boolean invoke(Object param1, Object param2)
    {
        // TODO try dynamic casting
        return switch (operator)
        {
            case "<" -> (double) param1 < (double) param2;
            case ">" -> (double) param1 > (double) param2;
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }
}
