package qoccinero;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    <T> void unary(Recipe.Unary<T> recipe)
    {
        cook(recipe.name(), unaryMain(recipe));
    }

    void cook(String recipeName, Consumer<MethodSpec.Builder> mainConsumer)
    {
        TypeSpec.Builder type = TypeSpec.classBuilder(recipeName)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        MethodSpec.Builder mainMethod = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class);

        mainConsumer.accept(mainMethod);

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

        nativeMain.addStatement(CodeBlock.of("$L.main()", recipeName));
    }

    private static <T> Consumer<MethodSpec.Builder> unaryMain(Recipe.Unary<T> recipe)
    {
        return method ->
        {
            Streams.batched(recipe.paramType().values(), 80).stream()
                .map(batch ->
                    batch.stream()
                        .map(toCode(recipe))
                        .collect(Collectors.collectingAndThen(
                            Collectors.toList()
                            , Qoccinero::appendLineEnd
                        ))
                )
                .flatMap(Collection::stream)
                .forEach(method::addCode);
        };
    }

    private static Collection<CodeBlock> appendLineEnd(Collection<CodeBlock> codeBlocks)
    {
        codeBlocks.add(CodeBlock.of("putchar('\\n');"));
        return codeBlocks;
    }

    private static <T> Function<T, CodeBlock> toCode(Recipe.Unary<T> recipe)
    {
        return value ->
        {
            return CodeBlock.of(
                "putchar($L == $L ? '.' : 'F'); // $L\n"
                , recipe.expected().apply(value)
                , recipe.function().apply(value)
                , recipe.paramType().toHex(value)
            );
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
