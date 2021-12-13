package org.example.ea.helpers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.graph.CheckCast;
import org.qbicc.graph.ParameterValue;
import org.qbicc.graph.Value;
import org.qbicc.graph.ValueHandle;
import org.qbicc.graph.atomic.WriteAccessMode;
import org.qbicc.type.ClassObjectType;
import org.qbicc.type.ObjectType;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.element.FieldElement;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.generic.ClassSignature;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

public class EscapeAnalysisPoet extends Helper
{
    static String name;
    static MethodSpec.Builder main;

    protected EscapeAnalysisPoet(Rule rule)
    {
        super(rule);
    }

    @SuppressWarnings("unused")
    public void create(String name, BasicBlockBuilder delegate)
    {
        EscapeAnalysisPoet.name = name;

        final MethodElement element = (MethodElement) delegate.getCurrentElement();
        final DefinedTypeDefinition enclosingType = element.getEnclosingType();
        main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("var eaFactory = $T.of()", EscapeAnalysisFactory.class)
            .addStatement("var elementBuilder = $T.builder($S, $T.VOID_METHOD_DESCRIPTOR)"
                , MethodElement.class
                , element.getName()
                , MethodDescriptor.class
            )
            .addStatement("methodElementBuilder.setEnclosingType(ea.definedType($S))"
                , enclosingType.getInternalName()
            )
            .addStatement("var intra = ea.newIntraBuilder(methodElementBuilder.build())");
    }

    @SuppressWarnings("unused")
    public void callNew(ClassObjectType type, Value typeId, Value size, Value align)
    {
        // TODO should be recursive over the superclasses
        // TODO should take into account interfaces
        main.addStatement(
            "intra.new_(ea.classObjectType($S, $L), $S, $S, $S)"
            , type.getDefinition().getInternalName()
            , type.hasSuperClass() ? type.getSuperClassType().getDefinition().getInternalName() : null
            , show(typeId)
            , show(size)
            , show(align)
        );
    }

    private String show(Object obj)
    {
        return obj.toString() + "::" + obj.getClass().toString();
    }

    @SuppressWarnings("unused")
    public void callInstanceFieldOf(ValueHandle handle, FieldElement field)
    {
        main.addStatement(
            "intra.instanceFieldOf($S, $S)"
            , show(handle)
            , show(field)
        );
    }

    @SuppressWarnings("unused")
    public void callStore(ValueHandle handle, Value value, WriteAccessMode mode)
    {
        main.addStatement(
            "intra.store($S, $S, $S)"
           , show(handle)
            , show(value)
            , show(mode)
        );
    }

    @SuppressWarnings("unused")
    public void callCall(ValueHandle target, List<Value> arguments)
    {
        main.addStatement(
            "intra.call($S, $S)"
            , show(target)
            , show(arguments)
        );
    }

    @SuppressWarnings("unused")
    public void callStartMethod(List<ParameterValue> params)
    {
        main.addStatement("var bbb = intra.getDelegate()");
        main.addStatement("var params = new $T()", ArrayList.class);
        for (ParameterValue param : params)
        {
            main.addStatement("params.add(bbb.parameter($L, $S, $L))", valueType(param), param.getLabel(), param.getIndex());
        }
        main.addStatement("intra.startMethod(params)");
    }

    private String valueType(ParameterValue param)
    {
        switch (param.getType().toString())
        {
            case "s32":
                return "ts.getSignedInteger32Type()";
            default:
                throw new RuntimeException("NYI");
        }
    }

    @SuppressWarnings("unused")
    public void callReturn(Value value)
    {
        main.addStatement(
            "intra.return_($S)"
            , show(value)
        );
    }

    @SuppressWarnings("unused")
    public void callThrow(Value value)
    {
        main.addStatement(
            "intra.throw_($S)"
            , show(value)
        );
    }

    @SuppressWarnings("unused")
    public void callCheckcast(Value value, Value toType, Value toDimensions, CheckCast.CastType kind, ObjectType expectedType)
    {
        main.addStatement("intra.checkcast($S)"
            , show(value)
            , show(toType)
            , show(toDimensions)
            , show(kind)
            , show(expectedType)
        );
    }

    @SuppressWarnings("unused")
    public void callFinish()
    {
        main.addStatement("intra.finish()");

        var helloWorld = TypeSpec.classBuilder("HelloWorld")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(main.build())
            .build();

        var javaFile = JavaFile.builder("com.example.helloworld", helloWorld)
            .build();

        try
        {
            javaFile.writeTo(System.out);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }
}
