package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ScriptsTest
{
    private static final Method DOUBLE_TO_RAW_LONG_BITS =
        CheckedFunction2.<String, Class<?>[], Method>of(Double.class::getMethod)
            .unchecked()
            .apply("doubleToRawLongBits", new Class[]{double.class});

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
            , is(equalTo("Double.doubleToRawLongBits(Double.MAX_VALUE)"))
        );
    }
}
