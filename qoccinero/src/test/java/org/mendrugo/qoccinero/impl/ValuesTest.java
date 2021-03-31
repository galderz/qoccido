package org.mendrugo.qoccinero.impl;

import io.vavr.collection.List;
import org.hamcrest.number.IsNaN;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasItems;

public class ValuesTest
{
    @Test
    void doubles()
    {
        assertThat(
            List.narrow(Values.values(1000, List.of(double.class)).head())
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
            (List<Double>) Values.values(1000, List.of(double.class)).head()
            , hasItem(IsNaN.notANumber())
        );
    }
}
