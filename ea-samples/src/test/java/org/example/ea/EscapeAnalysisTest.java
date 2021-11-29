package org.example.ea;

import org.junit.jupiter.api.Test;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;
import org.qbicc.type.definition.element.MethodElement;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

// TODO: https://github.com/qbicc/qbicc/blob/main/test-utils/src/main/java/org/qbicc/test/AbstractCompilerTestCase.java
public class EscapeAnalysisTest
{
    @Test
    public void test000()
    {
        final MethodElement.Builder methodElementBuilder = MethodElement.builder();
        methodElementBuilder.setName("blah");


        BasicBlockBuilder.simpleBuilder()

        assertThat(
            new EscapeAnalysisIntraMethodBuilder(
                new DummyCompilationContext()
                , new DummyBasicBlockBuilder(methodElementBuilder.build())
            )
            , is(notNullValue())
        );
    }
}
