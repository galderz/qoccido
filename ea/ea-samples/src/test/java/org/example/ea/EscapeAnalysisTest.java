package org.example.ea;

import org.example.ea.helpers.EscapeAnalysisFactory;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class EscapeAnalysisTest
{
    @Test
    public void test000()
    {
        final EscapeAnalysisFactory ea = EscapeAnalysisFactory.of();
        assertThat(ea.newIntraBuilder("testMethod", "TestClass"), is(notNullValue()));
    }
}
