package qoccido.test.generator;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Queue;

public class Generator implements AutoCloseable
{
    private static final Path TARGET = Path.of("target", "generated-test-sources");

    private final Queue<TestCase> testCases = new ArrayDeque<>();

    TestCase testCase(String name)
    {
        final var testCase = new TestCase(name);
        testCases.add(testCase);
        return testCase;
    }

    @Override
    public void close() throws Exception
    {
        testCases
            .forEach(TestCase::close);

        TypeSpec mainType = TypeSpec.classBuilder("Qoccido")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(nativeMain())
            .addMethod(javaMain())
            .build();

        JavaFile javaFile = JavaFile.builder("", mainType)
            .build();

        javaFile.writeTo(TARGET);
    }

    private MethodSpec javaMain()
    {
        return MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .build();
    }

    private MethodSpec nativeMain()
    {
        final var nativeMain = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "export"))
            .returns(int.class);

        testCases.stream()
            .map(TestCase::callCode)
            .forEach(nativeMain::addStatement);

        nativeMain.addStatement("return 0");
        return nativeMain.build();
    }

    static final class TestCase implements AutoCloseable
    {
        private final String name;
        private final TypeSpec.Builder typeBuilder;

        final MethodSpec.Builder methodBuilder;

        public TestCase(String name)
        {
            this.name = name;

            this.typeBuilder = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

            this.methodBuilder = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class);
        }

        CodeBlock callCode()
        {
            return CodeBlock.of("$L.main()", name);
        }

        @Override
        public void close()
        {
            methodBuilder.addStatement("putchar('\\n')");

            final var putchar = MethodSpec.methodBuilder("putchar")
                .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "extern"))
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

            try
            {
                javaFile.writeTo(Path.of("target", "generated-test-sources"));
            }
            catch (IOException e)
            {
                throw new UncheckedIOException(e);
            }
        }
    }
}
