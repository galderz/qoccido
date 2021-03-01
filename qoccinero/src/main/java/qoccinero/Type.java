package qoccinero;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import io.vavr.collection.List;

import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.function.Consumer;

public record Type(
    String name
    , List<StaticMethod> staticMethods
    , List<BinaryOperator> binaryOperators
)
{
    static Type of(TypeRecipe typeRecipe)
    {
        return new Type(
            typeRecipe.name()
            , typeRecipe.staticMethods().map(StaticMethod::of)
            , typeRecipe.binaryOperators().map(BinaryOperator::of)
        );
    }

    TypeSpec toTypeSpec()
    {
        TypeSpec.Builder typeBuilder = TypeSpec.classBuilder(name)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL);

        final var putchar = MethodSpec.methodBuilder("putchar")
            .addAnnotation(ClassName.get("cc.quarkus.qcc.runtime.CNative", "extern"))
            .addModifiers(Modifier.STATIC, Modifier.NATIVE)
            .addParameter(int.class, "arg")
            .returns(int.class)
            .build();

        typeBuilder.addMethod(putchar);

        MethodSpec.Builder mainMethod = MethodSpec.methodBuilder("main")
            .addModifiers(Modifier.STATIC);

        staticMethods()
            .map(StaticMethod::toMethodSpec)
            .forEach(addMethod(mainMethod, typeBuilder));

        binaryOperators()
            .map(BinaryOperator::toMethodSpec)
            .forEach(addMethod(mainMethod, typeBuilder));

        typeBuilder.addMethod(mainMethod.build());

        final var typeSpec = typeBuilder.build();
        final var javaFile = JavaFile
            .builder("", typeSpec)
            .build();

        try
        {
            javaFile.writeTo(Qoccinero.TARGET);
        }
        catch (IOException e)
        {
            throw new UncheckedIOException(e);
        }

        return typeSpec;
    }

    private Consumer<MethodSpec> addMethod(MethodSpec.Builder mainMethod, TypeSpec.Builder typeBuilder)
    {
        return methodSpec ->
        {
            typeBuilder.addMethod(methodSpec);
            mainMethod.addStatement(CodeBlock.of("$L()", methodSpec.name));
        };
    }
}
