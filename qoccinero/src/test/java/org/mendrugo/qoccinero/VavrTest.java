package org.mendrugo.qoccinero;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class VavrTest
{
    /**
     * e.g. 1 > Integer.compare(a, b)
     */
    @Test
    void constantGreaterThanCompare()
    {
        Function2<Integer, Integer, Boolean> isGreater = (a, b) -> a > b;
        Function2<Integer, Integer, Integer> compare = Integer::compare;

        assertThat(
            compare.andThen(isGreater.apply(1)).apply(10, 20)
            , is(true)
        );
    }

    /**
     * e.g. Integer.compare(a, b) < 1
     */
    @Test
    void compareAndThenIsGreater()
    {
        Function2<Integer, Integer, Boolean> isLess = (a, b) -> a < b;
        Function2<Integer, Integer, Integer> compare = Integer::compare;

        assertThat(
            compare.andThen(isLess.reversed().apply(1)).apply(10, 20)
            , is(true)
        );
    }

    /**
     * e.g. Double.doubleToRawLongBits(_) == Double.doubleToRawLongBits(_)
     */
    @Test
    void functionIsEqualsToFunction()
    {
        Function2<Long, Long, Boolean> isEquals = Long::equals;
        Function1<Double, Long> toRaw = Double::doubleToRawLongBits;

        assertThat(
            isEquals.apply(toRaw.apply(Double.MAX_VALUE), toRaw.apply(Double.MAX_VALUE))
            , is(true)
        );

        assertThat(
            isEquals.tupled().<Double>compose(d -> Tuple.of(toRaw.apply(d), toRaw.apply(d))).apply(Double.MAX_VALUE)
            , is(true)
        );
    }
}
