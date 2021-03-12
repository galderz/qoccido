package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.Function2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class InvokeTest
{
    private static final Method DOUBLE_TO_RAW_LONG_BITS =
        CheckedFunction2.<String, Class<?>[], Method>of(Double.class::getMethod)
            .unchecked()
            .apply("doubleToRawLongBits", new Class[]{double.class});

    private static final Method LONG_BITS_TO_DOUBLE =
        CheckedFunction2.<String, Class<?>[], Method>of(Double.class::getMethod)
            .unchecked()
            .apply("longBitsToDouble", new Class[]{long.class});

    /**
     * e.g. Double.doubleToRawLongBits(_)
     */
    @Test
    void invokeStaticCall()
    {
        final Class<?> type = Double.class;
        final var call = new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(new Hole()));

        assertThat(
            Invoke.invoke1(call).apply(Double.MAX_VALUE)
            , is(9218868437227405311L)
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(Double.longBitsToDouble(_))
     */
    @Test
    void invokeStaticCallChainOfTwo()
    {
        final Class<?> type = Double.class;

        final var call = new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(
            new StaticCall(LONG_BITS_TO_DOUBLE, type, List.of(new Hole()))
        ));

        assertThat(
            Invoke.invoke1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(Double.longBitsToDouble(Double.doubleToRawLongBits(Double.longBitsToDouble(_))))
     */
    @Test
    void invokeStaticCallChainOfFour()
    {
        final Class<?> type = Double.class;

        final var call = new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(
            new StaticCall(LONG_BITS_TO_DOUBLE, type, List.of(
                new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(
                    new StaticCall(LONG_BITS_TO_DOUBLE, type, List.of(new Hole()))
                ))
            ))
        ));

        assertThat(
            Invoke.invoke1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
        );
    }

//    @Test
//    void binary()
//    {
//        assertThat(
//            Invoke.invoke2(new BinaryCall("<")).apply(1, 2)
//            , is(true)
//        );
//    }


//    @Test
//    void compose2()
//    {
//        assertThat(
//            Invoke.compose2(
//                Invoke.invoke2(new BinaryCall("<"))
//                , (a, b) -> Integer.compare((int) a, (int) b)
//                , x -> 1
//            ).apply(1, 2)
//            , is(true)
//        );
//    }
}
