package org.mendrugo.qoccinero;

import org.hamcrest.number.IsNaN;
import org.junit.jupiter.api.Test;
import org.mendrugo.qoccinero.Values;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class ValuesTest
{
    @Test
    void doubles()
    {
        assertThat(
            Values.doubles()
            , hasItems(
                Double.NEGATIVE_INFINITY
                , Double.MIN_VALUE
                , -Double.MIN_VALUE
                , Double.MIN_NORMAL
                , -Double.MIN_NORMAL
                , Double.MAX_VALUE
                , -Double.MAX_VALUE
                , Double.POSITIVE_INFINITY
            )
        );

        assertThat(
            Values.doubles()
            , hasItem(IsNaN.notANumber())
        );
    }
}
