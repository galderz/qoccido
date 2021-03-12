package org.mendrugo.qoccinero;

import io.vavr.Function2;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class VavrTest
{
    @Test
    void compareAndThenIsGreater()
    {
        Function2<Integer, Integer, Boolean> isMore = (a, b) -> a > b;
        Function2<Integer, Integer, Integer> compare = Integer::compare;

        // 1 > Integer.compare(a, b)
        assertThat(
            compare.andThen(isMore.apply(1)).apply(10, 20)
            , is(true)
        );
    }
}
