package qoccido.test.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;

abstract class AbstractGenerator implements AutoCloseable
{
    final String name;
    final MethodSpec.Builder methodBuilder;
    private final TypeSpec.Builder typeBuilder;

    protected AbstractGenerator(String name)
    {
        this.name = name;

        this.methodBuilder = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args");

        this.typeBuilder = TypeSpec.classBuilder(name)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);
    }

    @Override
    public void close() throws Exception
    {
        methodBuilder.addStatement("putchar('\\n')");

        final var putchar = MethodSpec.methodBuilder("putchar")
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime", "CNative.extern"))
            .addModifiers(Modifier.STATIC, Modifier.NATIVE)
            .addParameter(int.class, "arg")
            .returns(int.class)
            .build();

        final var type = typeBuilder
            .addMethod(methodBuilder.build())
            .addMethod(putchar)
            .build();

        final var javaFile = JavaFile
            .builder("", type)
            .build();

        javaFile.writeTo(Path.of("target", "generated-test-sources"));
    }
}
