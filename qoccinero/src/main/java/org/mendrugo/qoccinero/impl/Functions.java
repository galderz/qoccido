package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;

public class Functions
{
    static Function1<Object, Object> function1(Expression expr)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            return v ->
                function2(binaryCall.operator()).apply(
                    function1(binaryCall.left()).apply(v)
                    , function1(binaryCall.right()).apply(v)
                );
        }

        if (expr instanceof Constant constant)
        {
            return ignore -> constant.value();
        }

        if (expr instanceof StaticCall staticCall)
        {
            final var head = staticCall.params().head();
            final var method = staticCall.method();
            final Class<?> type = staticCall.type();

            if (head instanceof Hole)
            {
                return Reflection.invoke1(method, type);
            }

            return Reflection.invoke1(method, type).compose(function1(head));
        }

        if (expr instanceof UnaryCall unaryCall)
        {
            return function1(unaryCall.operator()).compose(function1(unaryCall.expr()));
        }

        throw new RuntimeException("NYI");
    }

    static Function2<Object, Object, Object> function2(Expression expr)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            if (binaryCall.left() instanceof Hole && binaryCall.right() instanceof Hole)
            {
                return function2(binaryCall.operator());
            }

            return function2(binaryCall.left(), binaryCall.operator(), binaryCall.right());
        }

        if (expr instanceof StaticCall staticCall)
        {
            if (staticCall.params().head() instanceof Hole && staticCall.params().tail().head() instanceof Hole)
            {
                return Reflection.invoke2(staticCall.method(), staticCall.type());
            }
        }

        if (expr instanceof UnaryCall unaryCall)
        {
            return function2(unaryCall.expr()).andThen(function1(unaryCall.operator()));
        }

        throw new RuntimeException(String.format("NYI: %s", expr));
    }

    private static Function2<Object, Object, Object> function2(Expression left, String operator, Expression right)
    {
        if (left instanceof Constant constant)
        {
            return function2(right).andThen(function2(operator).apply(constant.value()));
        }

        if (right instanceof Constant constant)
        {
            return function2(left).andThen(function2(operator).reversed().apply(constant.value()));
        }

        return (a, b) ->
            function2(operator).apply(
                function1(left).apply(a)
                , function1(right).apply(b)
            );
    }

    private static Function1<Object, Object> function1(String operator)
    {
        return switch (operator)
        {
            case "-" -> Functions::negate;
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private static Number negate(Object v1)
    {
        return invokeUnary(
            v1
            , (d1) -> -d1
            , (f1) -> -f1
            , (i1) -> -i1
            , (l1) -> -l1
        );
    }

    private static Number invokeUnary(
        Object v1
        , Function1<Double, Double> doubleFn
        , Function1<Float, Float> floatFn
        , Function1<Integer, Integer> intFn
        , Function1<Long, Long> longFn
    )
    {
        if (v1 instanceof Double d1)
        {
            return doubleFn.apply(d1);
        }
        else if (v1 instanceof Float f1)
        {
            return floatFn.apply(f1);
        }
        else if (v1 instanceof Integer i1)
        {
            return intFn.apply(i1);
        }
        else if (v1 instanceof Long l1)
        {
            return longFn.apply(l1);
        }

        throw new RuntimeException(String.format(
            "invoke unary not handled for combination of v1 class %s"
            , v1.getClass()
        ));
    }

    private static Function2<Object, Object, Object> function2(String operator)
    {
        return switch (operator)
        {
            case "==" -> Functions::isEquals;
            case "<" -> Functions::isLess;
            case ">" -> Functions::isGreater;
            case "<=" -> (a, b) -> isLess(a, b) || isEquals(a, b);
            case ">=" -> (a, b) -> isGreater(a, b) || isEquals(a, b);
            default -> throw new IllegalStateException("Unexpected value: " + operator);
        };
    }

    private static Boolean isEquals(Object v1, Object v2)
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

    private static Boolean isLess(Object v1, Object v2)
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

    private static Boolean isGreater(Object v1, Object v2)
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

    private static Boolean invokeBinary(
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
