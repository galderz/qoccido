package org.example.ea.helpers;

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
import org.qbicc.tool.llvm.LlcInvoker;
import org.qbicc.tool.llvm.LlvmToolChain;
import org.qbicc.tool.llvm.OptInvoker;
import org.qbicc.type.ClassObjectType;
import org.qbicc.type.InterfaceObjectType;
import org.qbicc.type.TypeSystem;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.classfile.ClassFile;
import org.qbicc.type.definition.element.ExecutableElement;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.generic.ClassSignature;

import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EscapeAnalysisFactory
{
    public final CompilationContext ctxt;
    public final ClassContext bootClassContext;
    public final TypeSystem ts;

    public ClassObjectType classObjectType(String name, String superclassName)
    {
        if (superclassName == null)
        {
            return ts.generateClassObjectType(definedType("java/lang/Object"), null, null);
        }

        return ts.generateClassObjectType(definedType(name), classObjectType("java/lang/Object", null), List.of());
    }

    public DefinedTypeDefinition definedType(String name)
    {
        final DefinedTypeDefinition.Builder builder = DefinedTypeDefinition.Builder.basic();
        builder.setName(name);
        builder.setDescriptor(ClassTypeDescriptor.synthesize(bootClassContext, name));
        builder.setModifiers(ClassFile.ACC_SUPER | ClassFile.ACC_PUBLIC);
        builder.setSignature(ClassSignature.synthesize(bootClassContext, null, List.of()));
        builder.setSimpleName(name);
        builder.setInitializer((index, enclosing, b) -> b.build(), 0);
        return builder.build();
    }

    public EscapeAnalysisIntraMethodBuilder newIntraBuilder(final ExecutableElement element)
    {
        final BasicBlockBuilder bbb = new SimpleOptBasicBlockBuilder(ctxt, BasicBlockBuilder.simpleBuilder(ts, element));
        bbb.startMethod(List.of());
        bbb.begin(new BlockLabel());

        return new EscapeAnalysisIntraMethodBuilder(ctxt, bbb);
    }

    public static EscapeAnalysisFactory of()
    {
        final Driver.Builder builder = Driver.builder();
        builder.setInitialContext(new BaseDiagnosticContext());
        builder.setOutputDirectory(Path.of(System.getProperty("user.dir", "."), "target", "test-fwk"));
        final Platform platform = Platform.HOST_PLATFORM;
        builder.setTargetPlatform(platform);

        Optional<ObjectFileProvider> ofp = ObjectFileProvider.findProvider(platform.getObjectType(), EscapeAnalysisFactory.class.getClassLoader());
        if (ofp.isEmpty())
        {
            throw new RuntimeException("No object file provider found for " + platform);
        }
        builder.setObjectFileProvider(ofp.get());

        Iterator<CToolChain> toolChains = CToolChain.findAllCToolChains(platform, t -> true, EscapeAnalysisFactory.class.getClassLoader()).iterator();
        if (!toolChains.hasNext())
        {
            throw new RuntimeException("No tool chains found for " + platform);
        }
        builder.setToolChain(toolChains.next());

        Iterator<LlvmToolChain> llvmTools = List.<LlvmToolChain>of(new DummyLlvmToolChain()).iterator();
        builder.setLlvmToolChain(llvmTools.next());

        final TypeSystem.Builder tsBuilder = TypeSystem.builder();
        final TypeSystem ts = tsBuilder.build();
        builder.setTypeSystem(ts);

        builder.setVmFactory(CompilationContext::getVm);

        final Driver driver = builder.build();
        final CompilationContext ctxt = driver.getCompilationContext();
        final ClassContext bootClassContext = ctxt.getBootstrapClassContext();
        return new EscapeAnalysisFactory(ctxt, bootClassContext, ts);
    }

    private EscapeAnalysisFactory(CompilationContext ctxt, ClassContext bootClassContext, TypeSystem ts)
    {
        this.ctxt = ctxt;
        this.bootClassContext = bootClassContext;
        this.ts = ts;
    }

    private static final class DummyLlvmToolChain implements LlvmToolChain
    {
        @Override
        public LlcInvoker newLlcInvoker()
        {
            return null;  // TODO: Customise this generated block
        }

        @Override
        public OptInvoker newOptInvoker()
        {
            return null;  // TODO: Customise this generated block
        }

        @Override
        public Platform getPlatform()
        {
            return null;  // TODO: Customise this generated block
        }

        @Override
        public String getVersion()
        {
            return null;  // TODO: Customise this generated block
        }

        @Override
        public int compareVersionTo(String version)
        {
            return 0;  // TODO: Customise this generated block
        }
    }
}
