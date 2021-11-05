package org.example.ea;

import org.qbicc.context.AttachmentKey;
import org.qbicc.context.ClassContext;
import org.qbicc.context.CompilationContext;
import org.qbicc.context.Diagnostic;
import org.qbicc.context.Location;
import org.qbicc.graph.BasicBlock;
import org.qbicc.graph.Node;
import org.qbicc.graph.NodeVisitor;
import org.qbicc.graph.Value;
import org.qbicc.graph.ValueHandle;
import org.qbicc.graph.literal.LiteralFactory;
import org.qbicc.graph.literal.SymbolLiteral;
import org.qbicc.interpreter.Vm;
import org.qbicc.interpreter.VmClassLoader;
import org.qbicc.machine.arch.Platform;
import org.qbicc.object.Function;
import org.qbicc.object.FunctionDeclaration;
import org.qbicc.object.ProgramModule;
import org.qbicc.object.Section;
import org.qbicc.type.FunctionType;
import org.qbicc.type.TypeSystem;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.NativeMethodConfigurator;
import org.qbicc.type.definition.element.Element;
import org.qbicc.type.definition.element.ExecutableElement;
import org.qbicc.type.definition.element.FieldElement;
import org.qbicc.type.definition.element.MemberElement;
import org.qbicc.type.definition.element.MethodElement;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

final class DummyCompilationContext implements CompilationContext
{
    @Override
    public Platform getPlatform()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public TypeSystem getTypeSystem()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public LiteralFactory getLiteralFactory()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ClassContext getBootstrapClassContext()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ClassContext constructClassContext(VmClassLoader classLoaderObject)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public MethodElement getVMHelperMethod(String helperName)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public MethodElement getOMHelperMethod(String helperName)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public void enqueue(ExecutableElement element)
    {
        // TODO: Customise this generated block
    }

    @Override
    public boolean wasEnqueued(ExecutableElement element)
    {
        return false;  // TODO: Customise this generated block
    }

    @Override
    public NativeMethodConfigurator getNativeMethodConfigurator()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ExecutableElement dequeue()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public void registerEntryPoint(ExecutableElement method)
    {
        // TODO: Customise this generated block
    }

    @Override
    public Path getOutputDirectory()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Path getOutputFile(DefinedTypeDefinition type, String suffix)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Path getOutputDirectory(DefinedTypeDefinition type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Path getOutputDirectory(MemberElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ProgramModule getProgramModule(DefinedTypeDefinition type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ProgramModule getOrAddProgramModule(DefinedTypeDefinition type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public List<ProgramModule> getAllProgramModules()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public DefinedTypeDefinition getDefaultTypeDefinition()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Section getImplicitSection(ExecutableElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Section getImplicitSection(DefinedTypeDefinition typeDefinition)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Function getExactFunction(ExecutableElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Function getExactFunctionIfExists(ExecutableElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public FunctionType getFunctionTypeForElement(ExecutableElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public FunctionDeclaration declareForeignFunction(ExecutableElement target, Function function, ExecutableElement current)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public SymbolLiteral getCurrentThreadLocalSymbolLiteral()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public FieldElement getExceptionField()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Vm getVm()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public void setTaskRunner(BiConsumer<Consumer<CompilationContext>, CompilationContext> taskRunner) throws IllegalStateException
    {
        // TODO: Customise this generated block
    }

    @Override
    public void runParallelTask(Consumer<CompilationContext> task) throws IllegalStateException
    {
        // TODO: Customise this generated block
    }

    @Override
    public BiFunction<CompilationContext, NodeVisitor<Node.Copier, Value, Node, BasicBlock, ValueHandle>, NodeVisitor<Node.Copier, Value, Node, BasicBlock, ValueHandle>> getCopier()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T getAttachment(AttachmentKey<T> key)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T getAttachmentOrDefault(AttachmentKey<T> key, T defVal)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T putAttachment(AttachmentKey<T> key, T value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T putAttachmentIfAbsent(AttachmentKey<T> key, T value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T removeAttachment(AttachmentKey<T> key)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> boolean removeAttachment(AttachmentKey<T> key, T expect)
    {
        return false;  // TODO: Customise this generated block
    }

    @Override
    public <T> T replaceAttachment(AttachmentKey<T> key, T update)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> boolean replaceAttachment(AttachmentKey<T> key, T expect, T update)
    {
        return false;  // TODO: Customise this generated block
    }

    @Override
    public <T> T computeAttachmentIfAbsent(AttachmentKey<T> key, Supplier<T> function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T computeAttachmentIfPresent(AttachmentKey<T> key, java.util.function.Function<T, T> function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public <T> T computeAttachment(AttachmentKey<T> key, java.util.function.Function<T, T> function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public int errors()
    {
        return 0;  // TODO: Customise this generated block
    }

    @Override
    public int warnings()
    {
        return 0;  // TODO: Customise this generated block
    }

    @Override
    public Diagnostic msg(Diagnostic parent, Location location, Diagnostic.Level level, String fmt, Object... args)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Diagnostic msg(Diagnostic parent, Element element, Node node, Diagnostic.Level level, String fmt, Object... args)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Iterable<Diagnostic> getDiagnostics()
    {
        return null;  // TODO: Customise this generated block
    }
}
