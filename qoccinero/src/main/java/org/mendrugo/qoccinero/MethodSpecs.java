package org.mendrugo.qoccinero;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import javax.lang.model.element.Modifier;

final class MethodSpecs
{
    static <T> MethodSpec toMethodSpec1(
        ParamType<T> param
        , Function1<T, String> expected
        , Function1<T, String> actual
        , String methodName
    )
    {
        final var method = methodBuilder(methodName);

        final var values = Values.values(
            Unchecked.<ParamType<T>>cast(param).arbitrary()
        );

        values
            .map(v -> toCode(v, expected, actual))
            .append(CodeBlock.of("putchar('\\n'); \n"))
            .forEach(method::addCode);

        return method.build();
    }

    private static MethodSpec.Builder methodBuilder(String methodName)
    {
        final var method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.STATIC);

        List.ofAll(methodName.chars().boxed())
            .map(c -> CodeBlock.of("putchar((char) $L); \n", c))
            .append(CodeBlock.of("putchar('\\n'); \n"))
            .forEach(method::addCode);

        return method;
    }

    private static <T> CodeBlock toCode(
        T v
        , Function1<T, String> expected
        , Function1<T, String> actual
    )
    {
        return CodeBlock.of(
            "putchar($L == $L ? '.' : 'F');\n"
            , expected.apply(v)
            , actual.apply(v)
        );
    }

    static <T1, T2> MethodSpec toMethodSpec2(
        ParamType<T1> param1
        , ParamType<T2> param2
        , Function2<T1, T2, String> expected
        , Function2<T1, T2, String> actual
        , String methodName
    )
    {
        final var method = methodBuilder(methodName);

        final var values = Values.values(
            Unchecked.<ParamType<T1>>cast(param1).arbitrary()
            , Unchecked.<ParamType<T2>>cast(param2).arbitrary()
        );

        values
            .map(tuple2 -> toCode(tuple2._1, tuple2._2, expected, actual))
            .append(CodeBlock.of("putchar('\\n'); \n"))
            .forEach(method::addCode);

        return method.build();
    }

    private static <T1, T2> CodeBlock toCode(
        T1 v1
        , T2 v2
        , Function2<T1, T2, String> expected
        , Function2<T1, T2, String> actual
    )
    {
        return CodeBlock.of(
            "putchar($L == $L ? '.' : 'F');\n"
            , expected.apply(v1, v2)
            , actual.apply(v1, v2)
        );
    }

    static <T1, T2> MethodSpec toMethodSpec2(
        Expression<T1> expr1
        , Expression<T2> expr2
        , Function2<T1, T2, String> expected
        , Function2<String, String, String> actual
        , String methodName
    )
    {
        final var method = methodBuilder(methodName);

        // TODO centralize the number of iterations
        for (int i = 0; i < 1000; i++)
        {
            final var asserts1 = expr1.asserts();
            final var asserts2 = expr2.asserts();
            method.addCode(
                CodeBlock.of(
                    "putchar($L == $L ? '.' : 'F');\n"
                    , expected.apply(asserts1._1, asserts2._1)
                    , actual.apply(asserts1._2, asserts2._2)
                )
            );
        }

        method.addCode(CodeBlock.of("putchar('\\n'); \n"));
        return method.build();
    }

}
