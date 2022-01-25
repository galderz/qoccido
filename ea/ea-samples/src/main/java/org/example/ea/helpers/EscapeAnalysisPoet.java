package org.example.ea.helpers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;
import org.qbicc.graph.BasicBlockBuilder;
import org.qbicc.graph.CheckCast;
import org.qbicc.graph.ParameterValue;
import org.qbicc.graph.Value;
import org.qbicc.graph.ValueHandle;
import org.qbicc.graph.atomic.AccessModes;
import org.qbicc.graph.atomic.ReadAccessMode;
import org.qbicc.graph.atomic.WriteAccessMode;
import org.qbicc.plugin.opt.ea.EscapeAnalysisFactory;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;
import org.qbicc.type.ClassObjectType;
import org.qbicc.type.FunctionType;
import org.qbicc.type.ObjectType;
import org.qbicc.type.ValueType;
import org.qbicc.type.definition.DefinedTypeDefinition;
import org.qbicc.type.definition.element.ConstructorElement;
import org.qbicc.type.definition.element.FieldElement;
import org.qbicc.type.definition.element.MethodElement;
import org.qbicc.type.descriptor.ClassTypeDescriptor;
import org.qbicc.type.descriptor.MethodDescriptor;
import org.qbicc.type.descriptor.TypeDescriptor;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class EscapeAnalysisPoet extends Helper
{
    static String id;
    static MethodSpec.Builder main;
    static String eaFactory;
    static String intra;

    protected EscapeAnalysisPoet(Rule rule)
    {
        super(rule);
    }

    @SuppressWarnings("unused")
    public void create(String name, BasicBlockBuilder delegate)
    {
        EscapeAnalysisPoet.id = name.replaceAll("[./()]", "_");
        EscapeAnalysisPoet.eaFactory = "eaFactory";
        EscapeAnalysisPoet.intra = "intra";

        final MethodElement element = (MethodElement) delegate.getCurrentElement();
        final DefinedTypeDefinition enclosingType = element.getEnclosingType();
        main = MethodSpec.methodBuilder("get")
            .addModifiers(Modifier.PUBLIC)
            .returns(EscapeAnalysisIntraMethodBuilder.class);
    }

    @SuppressWarnings("unused")
    public void callNew(ClassObjectType type, Value typeId, Value size, Value align)
    {
        // TODO should be recursive over the superclasses
        // TODO should take into account interfaces
        String classObjectType = "classObjectType";
        main.addStatement(
            "var type = $N.definedType($S)"
            , eaFactory
            , type.getDefinition().getInternalName()
        );
        main.addStatement(
            "var $N = $N.classObjectType(type, $S)"
            , classObjectType
            , eaFactory
            , type.hasSuperClass() ? type.getSuperClassType().getDefinition().getInternalName() : null
        );
        main.addStatement(
            "var new_ = $N.new_($N, $N.literalOfType($N), $N.literalOf($S), $N.literalOf($S))"
            , intra
            , classObjectType
            , eaFactory
            , classObjectType
            , eaFactory
            , size.toString()
            , eaFactory
            , align.toString()
        );
    }

    public void callNew(ClassTypeDescriptor desc)
    {
        // TODO
    }

    private String show(Object obj)
    {
        return obj.toString() + "::" + obj.getClass().toString();
    }

    @SuppressWarnings("unused")
    public void callInstanceFieldOf(ValueHandle handle, FieldElement field)
    {
        main.addStatement(
            "var fieldOf = intra.instanceFieldOf($N, $N.fieldElement($S, $N.typeDescriptor($S), type))"
            , "ref"
            , eaFactory
            , field.getName()
            , eaFactory
            , field.getTypeDescriptor().toString()
        );
    }

    public void callInstanceFieldOf(ValueHandle handle, TypeDescriptor owner, String name, TypeDescriptor type)
    {
        // TODO
    }

    @SuppressWarnings("unused")
    public void callStore(ValueHandle handle, Value value, WriteAccessMode mode)
    {
        main.addStatement(
            "intra.store(fieldOf, p0, $T.$L)"
            , AccessModes.class
            , mode.toString()
        );
    }

    @SuppressWarnings("unused")
    public void callCall(ValueHandle target, List<Value> arguments)
    {
        main.addStatement(
            "intra.call(ctor, $T.of())"
            , List.class
        );
    }

    @SuppressWarnings("unused")
    public void callStartMethod(List<ParameterValue> params)
    {
        main.addStatement("var $N = $L.newIntraBuilder(methodName, className)", intra, eaFactory);
        main.addStatement("var bbb = $L.getDelegate()", intra);
        main.addStatement("var params = new $T<$T>()", ArrayList.class, ParameterValue.class);
        for (ParameterValue param : params)
        {
            main.addStatement(
                "var p$L = bbb.parameter($N.valueType($S), $S, $L)"
                , param.getIndex()
                , eaFactory
                , param.getType().toString()
                , param.getLabel()
                , param.getIndex()
            );
            main.addStatement(
                "params.add(p$L)"
                , param.getIndex()
            );
        }
        main.addStatement("intra.startMethod(params)");
    }

    @SuppressWarnings("unused")
    public void callReturn(Value value)
    {
        main.addStatement(
            "intra.return_(load)"
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

    public void callCheckcast(Value value, TypeDescriptor desc)
    {
        // TODO
    }

    @SuppressWarnings("unused")
    public void callFinish()
    {
        main.addStatement("intra.finish()");
        main.addStatement("return intra");

        var allFieldsConstructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addParameter(String.class, "id")
            .addParameter(String.class, "className")
            .addParameter(String.class, "methodName")
            .addStatement("this.$N = $N", "id", "id")
            .addStatement("this.$N = $N", "className", "className")
            .addStatement("this.$N = $N", "methodName", "methodName")
            .addStatement("this.$N = $T.of()", eaFactory, EscapeAnalysisFactory.class);

        var emptyConstructor = MethodSpec.constructorBuilder()
            .addModifiers(Modifier.PUBLIC)
            .addStatement(
                "this($S, $S, $S)"
                , "org/example/ea/samples.EASample_01_Basic.sample1(I)I"
                , "org/example/ea/samples/EASample_01_Basic"
                , "sample1"
            );

        var helloWorld = TypeSpec.classBuilder(EscapeAnalysisPoet.id)
            .addSuperinterface(ParameterizedTypeName.get(Supplier.class, EscapeAnalysisIntraMethodBuilder.class))
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addField(String.class, "id", Modifier.PRIVATE, Modifier.FINAL)
            .addField(String.class, "className", Modifier.PRIVATE, Modifier.FINAL)
            .addField(String.class, "methodName", Modifier.PRIVATE, Modifier.FINAL)
            .addField(EscapeAnalysisFactory.class, "eaFactory", Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(allFieldsConstructor.build())
            .addMethod(emptyConstructor.build())
            .addMethod(main.build())
            .build();

        var javaFile = JavaFile.builder("org.example.ea", helloWorld)
            .build();

        try
        {
            javaFile.writeTo(System.out);
            javaFile.writeTo(Path.of("..", "ea-jmh", "src", "main", "java"));
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }
    }

    @SuppressWarnings("unused")
    public void callReferenceHandle(Value reference)
    {
        main.addStatement("var ref = intra.referenceHandle(new_)");
    }

    @SuppressWarnings("unused")
    public void callParameter(ValueType type, String label, int index)
    {
        main.addStatement(
            "intra.parameter($S, $S, $S)"
            , show(type)
            , show(label)
            , show(index)
        );
    }

    @SuppressWarnings("unused")
    public void callLoad(ValueHandle handle, ReadAccessMode accessMode)
    {
        main.addStatement(
            "var load = intra.load(fieldOf, $T.$L)"
            , AccessModes.class
            , accessMode.toString()
        );
    }

    @SuppressWarnings("unused")
    public void callConstructorOf(Value instance, ConstructorElement constructor, MethodDescriptor callSiteDescriptor, FunctionType callSiteType)
    {
        main.addStatement(
            "var ctor = intra.constructorOf(new_, $N.constructorElement(type), $N.methodDescriptor(), $N.functionType())"
            , eaFactory
            , eaFactory
            , eaFactory
        );
    }
}
