package org.example.ea;

import org.junit.jupiter.api.Test;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;
import org.qbicc.type.definition.element.MethodElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class EscapeAnalysisTest
{
    @Test
    public void test000()
    {
        final MethodElement.Builder methodElementBuilder = MethodElement.builder();
        methodElementBuilder.setName("blah");

        assertThat(
            new EscapeAnalysisIntraMethodBuilder(
                new DummyCompilationContext()
                , new DummyBasicBlockBuilder(methodElementBuilder.build())
            )
            , is(notNullValue())
        );
    }
}
