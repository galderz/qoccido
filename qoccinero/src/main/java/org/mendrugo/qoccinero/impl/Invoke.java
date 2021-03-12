package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;
import org.mendrugo.qoccinero.Unchecked;

import java.lang.reflect.Method;

public class Invoke
{
    static Function1<Object, Object> invoke1(StaticCall call)
    {
        final var head = call.params().head();

        if (head instanceof Hole)
        {
            return invoke1(call.method(), call.type());
        }

        if (head instanceof StaticCall staticCallBefore)
        {
            return invoke1(call.method(), call.type()).compose(invoke1(staticCallBefore));
        }

        throw new RuntimeException("NYE");
    }

    private static <T1, R> Function1<T1, R> invoke1(Method method, Class<?> type)
    {
        return v -> Reflection.invoke(v, method, type);
    }

//    static <T1, T2, R> Function2<T1, T2, R> invoke2(BinaryCall call)
//    {
//        return switch (call.operator())
//        {
//            case "<" -> Invoke::less;
//            default -> throw new IllegalStateException("Unexpected value: " + call.operator());
//        };
//    }
//
//    static <V1, V2, T1, T2, R> Function2<V1, V2, R> compose2(
//        Function0<T1> before1
//        , Function2<T1, T2, R> binary
//        , Function2<V1, V2, T2> before2
//    )
//    {
//        return before2.andThen(binary.apply(before1.get()));
//    }


//    static <V1, V2, R> Function2<V1, V2, R> compose2(
//        Function2<Object, Object, Object> call
//        , Function1<Object, Object> before1
//        , Function1<Object, Object> before2
//    )
//    {
//        return (a, b) ->
//        {
//
//        }
//    }

    private static <T, R> R less(T v1, T v2)
    {
        return Unchecked.cast(
            invokeBinary(
                v1
                , v2
                , (d1, d2) -> d1 < d2
                , (f1, f2) -> f1 < f2
                , (i1, i2) -> i1 < i2
                , (l1, l2) -> l1 < l2
            )
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
