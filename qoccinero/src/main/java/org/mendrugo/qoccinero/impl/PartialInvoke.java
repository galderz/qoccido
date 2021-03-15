package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;

public class PartialInvoke
{
    static Function1<Object, Object> invoke1(Expression expr)
    {
        if (expr instanceof StaticCall staticCall)
        {
            final var head = staticCall.params().head();
            final var method = staticCall.method();
            final Class<?> type = staticCall.type();

            if (head instanceof Hole)
            {
                return Reflection.invoke1(method, type);
            }

            if (head instanceof StaticCall staticBefore)
            {
                return Reflection.invoke1(method, type).compose(invoke1(staticBefore));
            }
        }

        if (expr instanceof BinaryCall binaryCall)
        {
            return v ->
                invoke2(binaryCall.operator()).apply(
                    invoke1(binaryCall.left()).apply(v)
                    , invoke1(binaryCall.right()).apply(v)
                );
        }

        if (expr instanceof Constant constant)
        {
            return ignore -> constant.value();
        }

        throw new RuntimeException("NYI");
    }

    static Function2<Object, Object, Object> invoke2(Expression expr)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            if (binaryCall.left() instanceof Hole && binaryCall.right() instanceof Hole)
            {
                return invoke2(binaryCall.operator());
            }

            if (binaryCall.left() instanceof StaticCall && binaryCall.right() instanceof StaticCall)
            {
                return (a, b) ->
                    invoke2(binaryCall.operator()).apply(
                        invoke1(binaryCall.left()).apply(a)
                        , invoke1(binaryCall.right()).apply(b)
                    );
            }

            if (binaryCall.left() instanceof StaticCall && binaryCall.right() instanceof Constant)
            {
                return (a, b) ->
                    invoke2(binaryCall.operator()).apply(
                        invoke2(binaryCall.left()).apply(a, b)
                        , invoke1(binaryCall.right()).apply(null)
                    );
            }

            if (binaryCall.left() instanceof Constant && binaryCall.right() instanceof StaticCall)
            {
                return (a, b) ->
                    invoke2(binaryCall.operator()).apply(
                        invoke1(binaryCall.left()).apply(null)
                        , invoke2(binaryCall.right()).apply(a, b)
                    );
            }
        }

        if (expr instanceof StaticCall staticCall)
        {
            if (staticCall.params().head() instanceof Hole && staticCall.params().tail().head() instanceof Hole)
            {
                return Reflection.invoke2(staticCall.method(), staticCall.type());
            }
        }

        throw new RuntimeException("NYI");
    }

    private static Function2<Object, Object, Object> invoke2(String operator)
    {
        return switch (operator)
        {
            case "==" -> PartialInvoke::isEquals;
            case "<" -> PartialInvoke::isLess;
            case ">" -> PartialInvoke::isMore;
            case "<=" -> (a, b) -> isLess(a, b) || isEquals(a, b);
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

    private static Boolean isMore(Object v1, Object v2)
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
