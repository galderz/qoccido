package qoccinero;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import jdk.jshell.JShell;

import javax.lang.model.element.Modifier;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
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

    <T> void unary(RecipeOld.Unary<T> recipe)
    {
        cook(recipe.name(), unaryMain(recipe));
    }

    <T, U> void binary(RecipeOld.Binary<T, U> recipe)
    {
        cook(recipe.name(), binaryMain(recipe));
    }

    void cook(String recipeName, Consumer<MethodSpec.Builder> mainConsumer)
    {
        MethodSpec.Builder recipeMethod = MethodSpec.methodBuilder(recipeName)
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(void.class);

        recipeName.chars()
            .mapToObj(c -> CodeBlock.of("putchar((char) $L); \n", c))
            .collect(Collectors.collectingAndThen(
                Collectors.toList()
                , Qoccinero::appendLineEnd
            ))
            .forEach(recipeMethod::addCode);

        mainConsumer.accept(recipeMethod);

        mainTypeBuilder.addMethod(recipeMethod.build());

        nativeMain.addStatement(CodeBlock.of("$L()", recipeName));
    }

    private static <T> Consumer<MethodSpec.Builder> unaryMain(RecipeOld.Unary<T> recipe)
    {
        return method ->
        {
            Streams.batched(ParamType.values(recipe.paramType().arbitrary()), 80).stream()
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

    private <U, T> Consumer<MethodSpec.Builder> binaryMain(RecipeOld.Binary<T, U> recipe)
    {
        return method ->
        {
            Streams.batched(ParamType.values(recipe.firstType().arbitrary(), recipe.secondType().arbitrary()), 80).stream()
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
        codeBlocks.add(CodeBlock.of("putchar('\\n'); \n"));
        return codeBlocks;
    }

    private static <T> Function<T, CodeBlock> toCode(RecipeOld.Unary<T> recipe)
    {
        return value ->
            CodeBlock.of(
                "putchar($L == $L ? '.' : 'F'); // $L\n"
                , recipe.expected().apply(value)
                , recipe.function().apply(value)
                , recipe.paramType().toHex(value)
            );
    }

    private static <T, U> Function<Map.Entry<T, U>, CodeBlock> toCode(RecipeOld.Binary<T, U> recipe)
    {
        return entry ->
            CodeBlock.of(
                "putchar($L == $L ? '.' : 'F'); // $L:$L\n"
                , recipe.expected().apply(entry.getKey(), entry.getValue())
                , recipe.function().apply(entry.getKey(), entry.getValue())
                , recipe.firstType().toHex(entry.getKey())
                , recipe.secondType().toHex(entry.getValue())
            );
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

        // TODO move to each type
        final var putchar = MethodSpec.methodBuilder("putchar")
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "extern"))
            .addModifiers(Modifier.STATIC, Modifier.NATIVE)
            .addParameter(int.class, "arg")
            .returns(int.class)
            .build();

        final var mainType = this.mainTypeBuilder
            .addMethod(nativeMain.build())
            .addMethod(putchar)
            .build();

        JavaFile javaFile = JavaFile.builder("", mainType)
            .build();

        javaFile.writeTo(TARGET);
    }

    private void write(TypeRecipe typeRecipe)
    {
        final var type = Type.of(typeRecipe);
        final var typeSpec = type.toTypeSpec();
        nativeMain.addStatement(CodeBlock.of("$L.main()", typeSpec.name));
    }

    public static void main(String[] args) throws Exception
    {
        try(Qoccinero qoccinero = new Qoccinero())
        {
            // qoccinero.unary(Recetas.Double_doubleToLongBits);
//            qoccinero.unary(Recipes.Double_doubleToRawLongBits);
//            qoccinero.unary(Recipes.Double_longBitsToDouble);
//            qoccinero.unary(Recipes.Float_floatToRawIntBits);
//            qoccinero.unary(Recipes.Float_intBitsToFloat);
//            qoccinero.binary(Recipes.Character_compare);
//            qoccinero.binary(Recipes.Integer_compare);
//            qoccinero.binary(Recipes.Integer_compareUnsigned);
//            qoccinero.binary(Recipes.Integer_divideUnsigned);
//            qoccinero.binary(Recipes.Integer_remainderUnsigned);
//            qoccinero.binary(Recipes.Long_divideUnsigned);
//            qoccinero.binary(Recipes.Long_remainderUnsigned);
//            qoccinero.binary(Recipes.Short_compare);
//            qoccinero.binary(Recipes.Short_compareUnsigned);

//            qoccinero.write(
//                TypeRecipe.of("Arithmetic")
//                    .addStaticMethod(
//                        StaticMethodRecipe.of("compare", "Double")
//                    )
//            );

            qoccinero.write(
                TypeRecipe.of("Arithmetic")
                    .addStaticMethod(StaticMethodRecipe.of("compare", "Integer"))
                    .addStaticMethod(StaticMethodRecipe.of("compare", "Double"))
                    .addBinaryOperator(BinaryOperatorRecipe.of("<", double.class))
                    .addBinaryOperator(BinaryOperatorRecipe.of(">", double.class))
                    .addBinaryOperator(BinaryOperatorRecipe.of("<", float.class))
                    .addBinaryOperator(BinaryOperatorRecipe.of(">", float.class))
            );
        }
    }
}
