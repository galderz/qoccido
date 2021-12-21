package org.example.ea.helpers;

import org.qbicc.context.ClassContext;
import org.qbicc.context.CompilationContext;
import org.qbicc.driver.BaseDiagnosticContext;
import org.qbicc.driver.Driver;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.graph.BlockLabel;
import org.qbicc.graph.literal.IntegerLiteral;
import org.qbicc.graph.literal.TypeLiteral;
import org.qbicc.machine.arch.Platform;
import org.qbicc.machine.object.ObjectFileProvider;
import org.qbicc.machine.tool.CToolChain;
import org.qbicc.plugin.opt.SimpleOptBasicBlockBuilder;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;
import org.qbicc.tool.llvm.LlcInvoker;
import org.qbicc.tool.llvm.LlvmToolChain;
import org.qbicc.tool.llvm.OptInvoker;
import org.qbicc.type.ClassObjectType;
import org.qbicc.type.FunctionType;
import org.qbicc.type.TypeSystem;
import org.qbicc.type.ValueType;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.classfile.ClassFile;
import org.qbicc.type.definition.element.ConstructorElement;
import org.qbicc.type.definition.element.FieldElement;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.descriptor.TypeDescriptor;
import org.qbicc.type.generic.BaseTypeSignature;
import org.qbicc.type.generic.ClassSignature;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EscapeAnalysisFactory
{
    public final CompilationContext ctxt;
    public final ClassContext bootClassContext;
    public final TypeSystem typeSystem;

    public FunctionType functionType()
    {
        final TypeSystem typeSystem = bootClassContext.getTypeSystem();
        return typeSystem.getFunctionType(typeSystem.getVoidType());
    }

    public MethodDescriptor methodDescriptor()
    {
        return MethodDescriptor.VOID_METHOD_DESCRIPTOR;
    }

    public ConstructorElement constructorElement(DefinedTypeDefinition enclosingType)
    {
        final ConstructorElement.Builder ctorBuilder = ConstructorElement.builder(methodDescriptor());
        ctorBuilder.setEnclosingType(enclosingType);
        return ctorBuilder.build();
    }

    public FieldElement fieldElement(String name, TypeDescriptor typeDescriptor, DefinedTypeDefinition enclosingType)
    {
        final FieldElement.Builder fieldBuilder = FieldElement.builder(name, typeDescriptor);
        fieldBuilder.setEnclosingType(enclosingType);
        fieldBuilder.setSignature(BaseTypeSignature.I); // TODO hardcoded
        return fieldBuilder.build();
    }

    public <T extends TypeDescriptor> T typeDescriptor(String typeName)
    {
        final ByteBuffer buf = ByteBuffer.wrap(typeName.getBytes(StandardCharsets.UTF_8));
        return (T) TypeDescriptor.parse(bootClassContext, buf);
    }

    public ValueType valueType(String value)
    {
        return switch (value)
        {
            case "s32" -> typeSystem.getSignedInteger32Type();
            default -> throw new IllegalArgumentException("Unknown value type: " + value);
        };
    }

    public IntegerLiteral literalOf(String value)
    {
        final String[] elements = value.split(" ");
        return switch (elements[0])
        {
            case "s64", "s32" -> ctxt.getLiteralFactory().literalOf(Long.parseLong(elements[1]));
            default -> throw new IllegalArgumentException("Unknown literal: " + value);
        };
    }

    public TypeLiteral literalOfType(ClassObjectType classObjectType)
    {
        return ctxt.getLiteralFactory().literalOfType(classObjectType);
    }

    public ClassObjectType classObjectType(DefinedTypeDefinition definedType, String superclassName)
    {
        if (superclassName == null)
        {
            return typeSystem.generateClassObjectType(definedType("java/lang/Object"), null, null);
        }

        return typeSystem.generateClassObjectType(definedType, classObjectType(definedType("java/lang/Object"), null), List.of());
    }

    public DefinedTypeDefinition definedType(String name)
    {
        final DefinedTypeDefinition.Builder builder = DefinedTypeDefinition.Builder.basic();
        builder.setContext(bootClassContext);
        builder.setDescriptor(ClassTypeDescriptor.synthesize(bootClassContext, name));
        builder.setInitializer((index, enclosing, b) -> b.build(), 0);
        builder.setModifiers(ClassFile.ACC_SUPER | ClassFile.ACC_PUBLIC);
        builder.setName(name);
        builder.setSignature(ClassSignature.synthesize(bootClassContext, null, List.of()));
        builder.setSimpleName(name);
        return builder.build();
    }

    public EscapeAnalysisIntraMethodBuilder newIntraBuilder(String methodName, String className)
    {
        // TODO add support for method descriptor, for now assume void method
        final MethodElement.Builder builder = MethodElement.builder(methodName, methodDescriptor());
        builder.setEnclosingType(definedType(className));
        final MethodElement element = builder.build();

        final BasicBlockBuilder bbb = new SimpleOptBasicBlockBuilder(ctxt, BasicBlockBuilder.simpleBuilder(typeSystem, element));
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
        final EscapeAnalysisFactory factory = new EscapeAnalysisFactory(ctxt, bootClassContext, ts);
        final DefinedTypeDefinition objectType = factory.defineClass("java/lang/Object", null);
        factory.defineClass("java/lang/Thread", objectType);
        factory.defineClass("java/lang/ThreadGroup", objectType);
        return factory;
    }

    private DefinedTypeDefinition defineClass(String name, DefinedTypeDefinition superClass)
    {
        final DefinedTypeDefinition.Builder typeBuilder = bootClassContext.newTypeBuilder();
        typeBuilder.setContext(bootClassContext);
        typeBuilder.setDescriptor(typeDescriptor(String.format("L%s;", name)));
        typeBuilder.setInitializer((index, enclosing, builder) -> {
            builder.setEnclosingType(enclosing);
            return builder.build();
        }, 0);
        typeBuilder.setName(name);
        typeBuilder.setSignature(ClassSignature.synthesize(bootClassContext, null, List.of()));
        if (superClass != null)
            typeBuilder.setSuperClass(superClass);
        else
            typeBuilder.setSuperClassName(null);

        final DefinedTypeDefinition type = typeBuilder.build();
        bootClassContext.defineClass(name, type);
        return type;
    }

    private EscapeAnalysisFactory(CompilationContext ctxt, ClassContext bootClassContext, TypeSystem typeSystem)
    {
        this.ctxt = ctxt;
        this.bootClassContext = bootClassContext;
        this.typeSystem = typeSystem;
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
