package org.example.ea;

import org.junit.jupiter.api.Test;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EscapeAnalysisTest
{
    @Test
    public void test000()
    {
        assertThat(
            new EscapeAnalysisIntraMethodBuilder(
                new DummyCompilationContext()
                , new DummyBasicBlockBuilder()
            )
            , is(notNullValue())
        );
    }
}
