package org.mendrugo.qoccinero;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.vavr.Function1;
import io.vavr.Function2;

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
        final var method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.STATIC);

        final var values = Values.values(
            Unchecked.<ParamType<T>>cast(param).arbitrary()
        );

        values
            .map(v -> toCode(v, expected, actual))
            .append(CodeBlock.of("putchar('\\n'); \n"))
            .forEach(method::addCode);

        return method.build();
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
        final var method = MethodSpec.methodBuilder(methodName)
            .addModifiers(Modifier.STATIC);

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
}
