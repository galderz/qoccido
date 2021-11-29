package org.example.ea.helpers;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import org.jboss.byteman.rule.Rule;
import org.jboss.byteman.rule.helper.Helper;
import org.qbicc.graph.ParameterValue;
import org.qbicc.plugin.opt.ea.EscapeAnalysisIntraMethodBuilder;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

public class EventPoet extends Helper
{
    static String name;
    static MethodSpec.Builder main;

    protected EventPoet(Rule rule)
    {
        super(rule);
    }

    @SuppressWarnings("unused")
    public void create(String name)
    {
        EventPoet.name = name;

        main = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .addStatement("var intra = new $T(null, null)", EscapeAnalysisIntraMethodBuilder.class);
    }

    @SuppressWarnings("unused")
    public void startMethod(List<ParameterValue> params)
    {
        main.addStatement(String.valueOf(params.get(0).getType()));
        main.addStatement(params.get(0).getLabel());
        main.addStatement(String.valueOf(params.get(0).getIndex()));
        main.addStatement("intra.startMethod(List.of())");
    }

    @SuppressWarnings("unused")
    public void parameter()
    {
        main.addStatement("intra.parameter()");
    }

    @SuppressWarnings("unused")
    public void finish()
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
