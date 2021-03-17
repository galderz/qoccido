package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ScriptsTest
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
    void staticCall()
    {
        final Class<?> type = Double.class;
        final var call = new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(new Hole()));

        assertThat(
            Scripts.script1(call).apply("Double.MAX_VALUE")
            , is("Double.doubleToRawLongBits(Double.MAX_VALUE)")
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(Double.longBitsToDouble(_))
     */
    @Test
    void staticCallChainOfTwo()
    {
        final Class<?> type = Double.class;

        final var call = new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(
            new StaticCall(LONG_BITS_TO_DOUBLE, type, List.of(new Hole()))
        ));

        assertThat(
            Scripts.script1(call).apply("Long.MAX_VALUE")
            , is("Double.doubleToRawLongBits(Double.longBitsToDouble(Long.MAX_VALUE))")
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(Double.longBitsToDouble(Double.doubleToRawLongBits(Double.longBitsToDouble(_))))
     */
    @Test
    void staticCallChainOfFour()
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
            Scripts.script1(call).apply("Long.MAX_VALUE")
            , is("Double.doubleToRawLongBits(Double.longBitsToDouble(Double.doubleToRawLongBits(Double.longBitsToDouble(Long.MAX_VALUE))))")
        );
    }

    /**
     * e.g. 1 < 2
     */
    @Test
    void binaryCall()
    {
        assertThat(
            Scripts.script2(new BinaryCall(new Hole(), "<", new Hole())).apply("1", "2")
            , is("1 < 2")
        );
    }
}
