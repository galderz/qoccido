package qoccinero;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import net.jqwik.api.Shrinkable;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Qoccinero implements AutoCloseable
{
    static final Path TARGET = Path.of("target", "generated-sources");

    private final TypeSpec.Builder mainTypeBuilder;
    private final MethodSpec.Builder nativeMain;

    public Qoccinero()
    {
        this.mainTypeBuilder = TypeSpec.classBuilder("Qoccido")
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addMethod(javaMain());

        this.nativeMain = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "export"))
            .returns(int.class);
    }

    <T> void unary(Recipe<T> recipe)
    {
        TypeSpec.Builder type = TypeSpec.classBuilder(recipe.name)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder mainMethod = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class);

        AtomicInteger index = new AtomicInteger();
        recipe.paramType.arbitrary().sampleStream().limit(1000)
            .forEach(acceptUnary(recipe, mainMethod, index));
        recipe.paramType.arbitrary().edgeCases().suppliers().stream()
            .map(Supplier::get)
            .map(Shrinkable::value)
            .forEach(acceptUnary(recipe, mainMethod, index));

        mainMethod.addStatement("putchar('\\n')");

        final var putchar = MethodSpec.methodBuilder("putchar")
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "extern"))
            .addModifiers(Modifier.STATIC, Modifier.NATIVE)
            .addParameter(int.class, "arg")
            .returns(int.class)
            .build();

        type
            .addMethod(mainMethod.build())
            .addMethod(putchar);

        final var javaFile = JavaFile
            .builder("", type.build())
            .build();

        try
        {
            javaFile.writeTo(Qoccinero.TARGET);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }

        nativeMain.addStatement(CodeBlock.of("$L.main()", recipe.name));
    }

    private static <T> Consumer<T> acceptUnary(Recipe<T> recipe, MethodSpec.Builder mainMethod, AtomicInteger index)
    {
        return value ->
        {
            mainMethod.addCode(
                "putchar($L == $L ? '.' : 'F'); // $L\n"
                , recipe.expected.apply(value)
                , recipe.function.apply(value)
                , recipe.paramType.toHex(value)
            );

            if (index.incrementAndGet() % 80 == 0)
            {
                mainMethod.addStatement("putchar('\\n')");
            }
        };
    }

    private MethodSpec javaMain()
    {
        return MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class)
            .addParameter(String[].class, "args")
            .build();
    }

    @Override
    public void close() throws Exception
    {
        nativeMain.addStatement("return 0");

        final var mainType = this.mainTypeBuilder
            .addMethod(nativeMain.build())
            .build();

        JavaFile javaFile = JavaFile.builder("", mainType)
            .build();

        javaFile.writeTo(TARGET);
    }

    public static void main(String[] args) throws Exception
    {
        try(Qoccinero qoccinero = new Qoccinero())
        {
            // qoccinero.unary(Recetas.Double_doubleToLongBits);
            qoccinero.unary(Recipes.Double_doubleToRawLongBits);
            qoccinero.unary(Recipes.Double_longBitsToDouble);
            qoccinero.unary(Recipes.Float_floatToRawIntBits);
            qoccinero.unary(Recipes.Float_intBitsToFloat);
        }
    }
}
