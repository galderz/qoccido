package org.mendrugo.qoccinero;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import javax.lang.model.element.Modifier;

final class MethodSpecs
{
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

    static <T, R> MethodSpec toMethodSpec1(
        Expression<T> expr
        , Function1<T, R> expected
        , ParamType<R> returns
        , Function1<String, String> actual
        , String methodName
    )
    {
        final var method = methodBuilder(methodName);

        // TODO centralize the number of iterations (or fetch from the inputValues being iterated over?)
        for (int i = 0; i < 1000; i++)
        {
            final var expects = expr.expects();
            method.addCode(
                CodeBlock.of(
                    "putchar($L == $L ? '.' : 'F');\n"
                    , returns.toLiteral().apply(expected.apply(expects.value()))
                    , actual.apply(expects.createdBy())
                )
            );
        }

        method.addCode(CodeBlock.of("putchar('\\n'); \n"));

        return method.build();
    }

    static <T1, T2, R> MethodSpec toMethodSpec2(
        Expression<T1> expr1
        , Expression<T2> expr2
        , Function2<T1, T2, R> expected
        , Function2<String, String, String> actual
        , String methodName
    )
    {
        final var method = methodBuilder(methodName);

        // TODO centralize the number of iterations (or fetch from the inputValues being iterated over?)
        for (int i = 0; i < 1000; i++)
        {
            final var expects1 = expr1.expects();
            final var expects2 = expr2.expects();
            method.addCode(
                CodeBlock.of(
                    "putchar($L == ($L) ? '.' : 'F');\n"
                    , expected.apply(expects1.value(), expects2.value())
                    , actual.apply(expects1.createdBy(), expects2.createdBy())
                )
            );
        }

        method.addCode(CodeBlock.of("putchar('\\n'); \n"));
        return method.build();
    }

}
