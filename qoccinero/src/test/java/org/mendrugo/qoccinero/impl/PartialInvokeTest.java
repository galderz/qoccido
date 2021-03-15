package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class PartialInvokeTest
{
    private static final Method COMPARE =
        CheckedFunction2.<String, Class<?>[], Method>of(Integer.class::getMethod)
            .unchecked()
            .apply("compare", new Class[]{int.class, int.class});

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
            PartialInvoke.invoke1(call).apply(Double.MAX_VALUE)
            , is(9218868437227405311L)
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
            PartialInvoke.invoke1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
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
            PartialInvoke.invoke1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
        );
    }

    /**
     * e.g. 1 < 2
     */
    @Test
    void binaryCall()
    {
        assertThat(
            PartialInvoke.invoke2(new BinaryCall(new Hole(), "<", new Hole())).apply(1, 2)
            , is(true)
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(_) == Double.doubleToRawLongBits(_)
     */
    @Test
    void binaryCallChain()
    {
        final var call = new BinaryCall(
            new StaticCall(DOUBLE_TO_RAW_LONG_BITS, Double.class, List.of(new Hole()))
            , "=="
            , new StaticCall(DOUBLE_TO_RAW_LONG_BITS, Double.class, List.of(new Hole()))
        );

        assertThat(
            PartialInvoke.invoke2(call).apply(Double.MAX_VALUE, Double.MAX_VALUE)
            , is(true)
        );
    }

    /**
     * e.g. C == Double.doubleToRawLongBits(_)
     */
    @Test
    void binaryCallConstantStatic()
    {
        final var call = new BinaryCall(
            new Constant(9218868437227405311L)
            , "=="
            , new StaticCall(DOUBLE_TO_RAW_LONG_BITS, Double.class, List.of(new Hole()))
        );

        assertThat(
            PartialInvoke.invoke1(call).apply(Double.MAX_VALUE)
            , is(true)
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(_) == C
     */
    @Test
    void binaryStaticCallConstant()
    {
        final var call = new BinaryCall(
            new StaticCall(DOUBLE_TO_RAW_LONG_BITS, Double.class, List.of(new Hole()))
            , "=="
            , new Constant(9218868437227405311L)
        );

        assertThat(
            PartialInvoke.invoke1(call).apply(Double.MAX_VALUE)
            , is(true)
        );
    }

    /**
     * e.g. Integer.compare(_, _)
     */
    @Test
    void staticBiCall()
    {
        final var call = new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole()));

        assertThat(
            PartialInvoke.invoke2(call).apply(123, 123)
            , is(equalTo(0))
        );
    }

    /**
     * e.g. Integer.compare(_, _) > 0
     */
    @Test
    void binaryStaticBiCallConstant()
    {
        final var call = new BinaryCall(
            new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole()))
            , ">"
            , new Constant(0)
        );

        assertThat(
            PartialInvoke.invoke2(call).apply(2, 1)
            , is(true)
        );
    }

    /**
     * e.g. -1 <= Integer.compare(_, _)
     */
    @Test
    void binaryConstantStaticBiCall()
    {
        final var call = new BinaryCall(
            new Constant(-1)
            , "<="
            , new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole()))
        );

        assertThat(
            PartialInvoke.invoke2(call).apply(3, 3)
            , is(true)
        );
    }

    /**
     * e.g. -Double.doubleToRawLongBits(_)
     */
    @Test
    void unaryStaticCall()
    {
        final Class<?> type = Double.class;
        final var call = new UnaryCall("-", new StaticCall(DOUBLE_TO_RAW_LONG_BITS, type, List.of(new Hole())));

        assertThat(
            PartialInvoke.invoke1(call).apply(Double.MAX_VALUE)
            , is(-9218868437227405311L)
        );
    }

    /**
     * e.g. -Integer.compare(_, _)
     */
    @Test
    void unaryStaticBiCall()
    {
        final var call = new UnaryCall(
            "-"
            , new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole()))
        );

        assertThat(
            PartialInvoke.invoke2(call).apply(8, 4)
            , is(equalTo(-1))
        );
    }

//    /**
//     * e.g. 0 >= -Integer.compare(_, _)
//     */
//    @Test
//    void binaryConstantUnaryStaticBiCall()
//    {
//        final var call = new BinaryCall(
//            new Constant(0)
//            , ">="
//            , new UnaryCall("-", new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole())))
//        );
//
//        assertThat(
//            PartialInvoke.invoke2(call).apply(8, 4)
//            , is(true)
//        );
//    }

//    /**
//     * e.g. -Integer.compare(_, _) < 1
//     */
//    @Test
//    void binaryUnaryStaticBiCallConstant()
//    {
//        final var call = new BinaryCall(
//            new UnaryCall("-", new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole())))
//            , "<"
//            , new Constant(1)
//        );
//
//        assertThat(
//            PartialInvoke.invoke2(call).apply(4, 8)
//            , is(true)
//        );
//    }
}
