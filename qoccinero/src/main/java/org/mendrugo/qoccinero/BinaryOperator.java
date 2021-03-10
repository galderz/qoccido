package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.Function2;

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
            Expression.of(recipe.left())
            , recipe.operator()
            , Expression.of(recipe.right())
            , ParamType.booleanType()
        );
    }

    MethodSpec toMethodSpec()
    {
        if (isLiteralBinaryOperator())
        {
            return toNonInlinedMethodSpec();
        }

        return toInlinedMethodSpec();
    }

    private boolean isLiteralBinaryOperator()
    {
        return left instanceof Literal<?> && right instanceof Literal<?>;
    }

    private MethodSpec toInlinedMethodSpec()
    {
        return MethodSpecs.toMethodSpec2(
            left
            , right
            , expected2()
            , (v1, v2) -> String.format("%s %s %s", v1, operator, v2)
            , operatorMethodName()
        );
    }

    MethodSpec toNonInlinedMethodSpec()
    {
        return MethodSpecs.toMethodSpec2(
            left
            , right
            , expected2()
            , (v1, v2) -> String.format("%s(%s, %s)", operatorLiteral(), v1, v2)
            , operatorMethodName()
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

    private String operatorLiteral()
    {
        return switch (operator)
        {
            case "<" -> "isLess";
            case "<=" -> "lessEquals";
            case ">" -> "isGreater";
            case ">=" -> "greaterEquals";
            case "==" -> "equals";
            case "!=" -> "notEquals";
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private boolean invoke(Object v1, Object v2)
    {
        return switch (operator)
        {
            case "<" -> less(v1, v2);
            case "<=" -> lessEquals(v1, v2);
            case ">" -> greater(v1, v2);
            case ">=" -> greaterEquals(v1, v2);
            case "==" -> equals(v1, v2);
            case "!=" -> !equals(v1, v2);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private boolean less(Object v1, Object v2)
    {
        return invokeBinary(
            v1
            , v2
            , (d1, d2) -> d1 < d2
            , (f1, f2) -> f1 < f2
            , (i1, i2) -> i1 < i2
            , (l1, l2) -> l1 < l2
        );
    }

    private boolean lessEquals(Object v1, Object v2)
    {
        return invokeBinary(
            v1
            , v2
            , (d1, d2) -> d1 <= d2
            , (f1, f2) -> f1 <= f2
            , (i1, i2) -> i1 <= i2
            , (l1, l2) -> l1 <= l2
        );
    }

    private boolean greater(Object v1, Object v2)
    {
        return invokeBinary(
            v1
            , v2
            , (d1, d2) -> d1 > d2
            , (f1, f2) -> f1 > f2
            , (i1, i2) -> i1 > i2
            , (l1, l2) -> l1 > l2
        );
    }

    private boolean greaterEquals(Object v1, Object v2)
    {
        return invokeBinary(
            v1
            , v2
            , (d1, d2) -> d1 >= d2
            , (f1, f2) -> f1 >= f2
            , (i1, i2) -> i1 >= i2
            , (l1, l2) -> l1 >= l2
        );
    }

    private boolean equals(Object v1, Object v2)
    {
        return invokeBinary(
            v1
            , v2
            , Double::equals
            , Float::equals
            , Integer::equals
            , Long::equals
        );
    }

    private boolean invokeBinary(
        Object v1
        , Object v2
        , Function2<Double, Double, Boolean> doubleFn
        , Function2<Float, Float, Boolean> floatFn
        , Function2<Integer, Integer, Boolean> intFn
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
        else if (v1 instanceof Integer i1)
        {
            if (v2 instanceof Integer i2)
            {
                return intFn.apply(i1, i2);
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
            "invoke binary not handled for combination of v1 class %s and v2 class %s"
            , v1.getClass()
            , v2.getClass()
        ));
    }
}
