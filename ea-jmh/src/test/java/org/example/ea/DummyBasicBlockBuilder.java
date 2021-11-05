package org.example.ea;

import org.qbicc.context.Location;
import org.qbicc.graph.BasicBlock;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.graph.BlockEntry;
import org.qbicc.graph.BlockLabel;
import org.qbicc.graph.CheckCast;
import org.qbicc.graph.CmpAndSwap;
import org.qbicc.graph.MemoryAtomicityMode;
import org.qbicc.graph.Node;
import org.qbicc.graph.ParameterValue;
import org.qbicc.graph.PhiValue;
import org.qbicc.graph.Value;
import org.qbicc.graph.ValueHandle;
import org.qbicc.graph.literal.BlockLiteral;
import org.qbicc.object.Function;
import org.qbicc.object.FunctionDeclaration;
import org.qbicc.type.ArrayObjectType;
import org.qbicc.type.ClassObjectType;
import org.qbicc.type.CompoundType;
import org.qbicc.type.ObjectType;
import org.qbicc.type.ReferenceType;
import org.qbicc.type.ValueType;
import org.qbicc.type.WordType;
import org.qbicc.type.definition.element.ConstructorElement;
import org.qbicc.type.definition.element.ExecutableElement;
import org.qbicc.type.definition.element.FieldElement;
import org.qbicc.type.definition.element.FunctionElement;
import org.qbicc.type.definition.element.GlobalVariableElement;
import org.qbicc.type.definition.element.LocalVariableElement;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ArrayTypeDescriptor;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.descriptor.TypeDescriptor;

import java.util.List;

final class DummyBasicBlockBuilder implements BasicBlockBuilder
{
    @Override
    public BasicBlockBuilder getFirstBuilder()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public void setFirstBuilder(BasicBlockBuilder first)
    {
        // TODO: Customise this generated block
    }

    @Override
    public ExecutableElement getCurrentElement()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ExecutableElement getRootElement()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ExecutableElement setCurrentElement(ExecutableElement element)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node getCallSite()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node setCallSite(Node callSite)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Location getLocation()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public int setLineNumber(int newLineNumber)
    {
        return 0;  // TODO: Customise this generated block
    }

    @Override
    public int setBytecodeIndex(int newBytecodeIndex)
    {
        return 0;  // TODO: Customise this generated block
    }

    @Override
    public ExceptionHandler getExceptionHandler()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public void setExceptionHandlerPolicy(ExceptionHandlerPolicy policy)
    {
        // TODO: Customise this generated block
    }

    @Override
    public void startMethod(List<ParameterValue> arguments)
    {
        // TODO: Customise this generated block
    }

    @Override
    public void finish()
    {
        // TODO: Customise this generated block
    }

    @Override
    public BasicBlock getFirstBlock() throws IllegalStateException
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ParameterValue parameter(ValueType type, String label, int index)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value currentThread()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value offsetOfField(FieldElement fieldElement)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value extractElement(Value array, Value index)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value extractMember(Value compound, CompoundType.Member member)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value extractInstanceField(Value valueObj, TypeDescriptor owner, String name, TypeDescriptor type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value extractInstanceField(Value valueObj, FieldElement field)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value insertElement(Value array, Value index, Value value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value insertMember(Value compound, CompoundType.Member member, Value value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node declareDebugAddress(LocalVariableElement variable, Value address)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public PhiValue phi(ValueType type, BlockLabel owner, PhiValue.Flag... flags)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value select(Value condition, Value trueValue, Value falseValue)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value add(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value multiply(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value and(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value or(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value xor(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isEq(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isNe(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value shr(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value shl(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value sub(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value divide(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value remainder(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value min(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value max(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isLt(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isGt(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isLe(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value isGe(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value rol(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value ror(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value cmp(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value cmpG(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value cmpL(Value v1, Value v2)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value notNull(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value negate(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value byteSwap(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value bitReverse(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value countLeadingZeros(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value countTrailingZeros(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value populationCount(Value v)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value arrayLength(ValueHandle arrayHandle)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value typeIdOf(ValueHandle valueHandle)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value classOf(Value typeId, Value dims)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value truncate(Value value, WordType toType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value extend(Value value, WordType toType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value bitCast(Value value, WordType toType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value valueConvert(Value value, WordType toType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value instanceOf(Value input, ObjectType expectedType, int expectedDimensions)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value instanceOf(Value input, TypeDescriptor desc)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value checkcast(Value value, Value toType, Value toDimensions, CheckCast.CastType kind, ReferenceType type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value checkcast(Value value, TypeDescriptor desc)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle memberOf(ValueHandle structHandle, CompoundType.Member member)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle elementOf(ValueHandle array, Value index)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle unsafeHandle(ValueHandle base, Value offset, ValueType outputType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle pointerHandle(Value pointer)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle referenceHandle(Value reference)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle instanceFieldOf(ValueHandle instance, FieldElement field)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle instanceFieldOf(ValueHandle instance, TypeDescriptor owner, String name, TypeDescriptor type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle staticField(FieldElement field)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle staticField(TypeDescriptor owner, String name, TypeDescriptor type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle globalVariable(GlobalVariableElement variable)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle localVariable(LocalVariableElement variable)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle exactMethodOf(Value instance, MethodElement method)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle exactMethodOf(Value instance, TypeDescriptor owner, String name, MethodDescriptor descriptor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle virtualMethodOf(Value instance, MethodElement method)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle virtualMethodOf(Value instance, TypeDescriptor owner, String name, MethodDescriptor descriptor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle interfaceMethodOf(Value instance, MethodElement method)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle interfaceMethodOf(Value instance, TypeDescriptor owner, String name, MethodDescriptor descriptor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle staticMethod(MethodElement method)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle staticMethod(TypeDescriptor owner, String name, MethodDescriptor descriptor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle constructorOf(Value instance, ConstructorElement constructor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle constructorOf(Value instance, TypeDescriptor owner, MethodDescriptor descriptor)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle functionOf(FunctionElement function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle functionOf(Function function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public ValueHandle functionOf(FunctionDeclaration function)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value addressOf(ValueHandle handle)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value referenceTo(ValueHandle handle) throws IllegalArgumentException
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value stackAllocate(ValueType type, Value count, Value align)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value new_(ClassObjectType type)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value new_(ClassTypeDescriptor desc)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value newArray(ArrayObjectType arrayType, Value size)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value newArray(ArrayTypeDescriptor desc, Value size)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value multiNewArray(ArrayObjectType arrayType, List<Value> dimensions)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value multiNewArray(ArrayTypeDescriptor desc, List<Value> dimensions)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value clone(Value object)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value load(ValueHandle handle, MemoryAtomicityMode mode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndAdd(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndBitwiseAnd(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndBitwiseNand(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndBitwiseOr(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndBitwiseXor(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndSet(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndSetMax(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndSetMin(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value getAndSub(ValueHandle target, Value update, MemoryAtomicityMode atomicityMode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value cmpAndSwap(ValueHandle target, Value expect, Value update, MemoryAtomicityMode successMode, MemoryAtomicityMode failureMode, CmpAndSwap.Strength strength)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node store(ValueHandle handle, Value value, MemoryAtomicityMode mode)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node classInitCheck(ObjectType objectType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node fence(MemoryAtomicityMode fenceType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node monitorEnter(Value obj)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node monitorExit(Value obj)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value call(ValueHandle target, List<Value> arguments)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value callNoSideEffects(ValueHandle target, List<Value> arguments)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node nop()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Node begin(BlockLabel blockLabel)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock callNoReturn(ValueHandle target, List<Value> arguments)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock invokeNoReturn(ValueHandle target, List<Value> arguments, BlockLabel catchLabel)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock tailCall(ValueHandle target, List<Value> arguments)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock tailInvoke(ValueHandle target, List<Value> arguments, BlockLabel catchLabel)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public Value invoke(ValueHandle target, List<Value> arguments, BlockLabel catchLabel, BlockLabel resumeLabel)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock goto_(BlockLabel resumeLabel)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock if_(Value condition, BlockLabel trueTarget, BlockLabel falseTarget)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock return_()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock return_(Value value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock unreachable()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock throw_(Value value)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock switch_(Value value, int[] checkValues, BlockLabel[] targets, BlockLabel defaultTarget)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock jsr(BlockLabel subLabel, BlockLiteral returnAddress)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock ret(Value address)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock classCastException(Value fromType, Value toType)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock noSuchMethodError(ObjectType owner, MethodDescriptor desc, String name)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock classNotFoundError(String name)
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BlockEntry getBlockEntry()
    {
        return null;  // TODO: Customise this generated block
    }

    @Override
    public BasicBlock getTerminatedBlock()
    {
        return null;  // TODO: Customise this generated block
    }
}
