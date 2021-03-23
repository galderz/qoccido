package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.collection.List;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class ImplTest
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
            Functions.function1(call).apply(Double.MAX_VALUE)
            , is(9218868437227405311L)
        );

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
            Functions.function1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
        );

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
            Functions.function1(call).apply(Long.MAX_VALUE)
            , is(Long.MAX_VALUE)
        );

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
            Functions.function2(new BinaryCall(new Hole(), "<", new Hole())).apply(1, 2)
            , is(true)
        );

        assertThat(
            Scripts.script2(new BinaryCall(new Hole(), "<", new Hole())).apply("1", "2")
            , is("1 < 2")
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
            Functions.function2(call).apply(Double.MAX_VALUE, Double.MAX_VALUE)
            , is(true)
        );

        assertThat(
            Scripts.script2(call).apply("Double.MAX_VALUE", "Double.MAX_VALUE")
            , is("Double.doubleToRawLongBits(Double.MAX_VALUE) == Double.doubleToRawLongBits(Double.MAX_VALUE)")
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
            Functions.function1(call).apply(Double.MAX_VALUE)
            , is(true)
        );

        assertThat(
            Scripts.script1(call).apply("Double.MAX_VALUE")
            , is("9218868437227405311L /* 0x7FEF_FFFF_FFFF_FFFFL */ == Double.doubleToRawLongBits(Double.MAX_VALUE)")
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
            Functions.function1(call).apply(Double.MAX_VALUE)
            , is(true)
        );

        assertThat(
            Scripts.script1(call).apply("Double.MAX_VALUE")
            , is("Double.doubleToRawLongBits(Double.MAX_VALUE) == 9218868437227405311L /* 0x7FEF_FFFF_FFFF_FFFFL */")
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
            Functions.function2(call).apply(123, 123)
            , is(0)
        );

        assertThat(
            Scripts.script2(call).apply("123", "123")
            , is("Integer.compare(123, 123)")
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
            Functions.function2(call).apply(2, 1)
            , is(true)
        );

        assertThat(
            Scripts.script2(call).apply("2", "1")
            , is("Integer.compare(2, 1) > 0 /* 0x0000_0000 */")
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
            Functions.function2(call).apply(3, 3)
            , is(true)
        );

        assertThat(
            Scripts.script2(call).apply("3", "3")
            , is("-1 /* 0xFFFF_FFFF */ <= Integer.compare(3, 3)")
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
            Functions.function1(call).apply(Double.MAX_VALUE)
            , is(-9218868437227405311L)
        );

        assertThat(
            Scripts.script1(call).apply("Double.MAX_VALUE")
            , is("-Double.doubleToRawLongBits(Double.MAX_VALUE)")
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
            Functions.function2(call).apply(8, 4)
            , is(-1)
        );

        assertThat(
            Scripts.script2(call).apply("8", "4")
            , is("-Integer.compare(8, 4)")
        );
    }

    /**
     * e.g. 0 >= -Integer.compare(_, _)
     */
    @Test
    void binaryConstantUnaryStaticBiCall()
    {
        final var call = new BinaryCall(
            new Constant(0)
            , ">="
            , new UnaryCall("-", new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole())))
        );

        assertThat(
            Functions.function2(call).apply(8, 4)
            , is(true)
        );

        assertThat(
            Scripts.script2(call).apply("8", "4")
            , is("0 /* 0x0000_0000 */ >= -Integer.compare(8, 4)")
        );
    }

    /**
     * e.g. -Integer.compare(_, _) < 1
     */
    @Test
    void binaryUnaryStaticBiCallConstant()
    {
        final var call = new BinaryCall(
            new UnaryCall("-", new StaticCall(COMPARE, Integer.class, List.of(new Hole(), new Hole())))
            , "<"
            , new Constant(1)
        );

        assertThat(
            Functions.function2(call).apply(8, 4)
            , is(true)
        );

        assertThat(
            Scripts.script2(call).apply("8", "4")
            , is("-Integer.compare(8, 4) < 1 /* 0x0000_0001 */")
        );
    }
}
