package org.example.ea;

import org.example.ea.helpers.EscapeAnalysisFactory;
import org.junit.jupiter.api.Test;
import org.qbicc.context.ClassContext;
import org.qbicc.context.CompilationContext;
import org.qbicc.driver.BaseDiagnosticContext;
import org.qbicc.driver.Driver;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.graph.BlockLabel;
import org.qbicc.machine.arch.Platform;
import org.qbicc.machine.object.ObjectFileProvider;
import org.qbicc.machine.tool.CToolChain;
import org.qbicc.plugin.opt.SimpleOptBasicBlockBuilder;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;
import org.qbicc.tool.llvm.LlvmToolChain;
import org.qbicc.type.TypeSystem;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.classfile.ClassFile;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.generic.ClassSignature;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import static io.smallrye.common.constraint.Assert.unreachableCode;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.fail;

// TODO: https://github.com/qbicc/qbicc/blob/main/test-utils/src/main/java/org/qbicc/test/AbstractCompilerTestCase.java
public class EscapeAnalysisTest
{
    @Test
    public void test000()
    {
        final EscapeAnalysisFactory ea = EscapeAnalysisFactory.of();

        final DefinedTypeDefinition.Builder typeBuilder = DefinedTypeDefinition.Builder.basic();
        typeBuilder.setName("TestClass");
        typeBuilder.setDescriptor(ClassTypeDescriptor.synthesize(ea.bootClassContext, "TestClass"));
        typeBuilder.setModifiers(ClassFile.ACC_SUPER | ClassFile.ACC_PUBLIC);
        typeBuilder.setSignature(ClassSignature.synthesize(ea.bootClassContext, null, List.of()));
        typeBuilder.setSimpleName("TestClass");
        typeBuilder.setInitializer((index, enclosing, b) -> b.build(), 0);

        final MethodElement.Builder methodElementBuilder = MethodElement.builder("testMethod", MethodDescriptor.VOID_METHOD_DESCRIPTOR);
        methodElementBuilder.setEnclosingType(typeBuilder.build());

        assertThat(ea.newIntraBuilder(methodElementBuilder.build()), is(notNullValue()));
    }
}
