package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.lang.reflect.Method;
import java.util.Arrays;

final class Scripts
{
    // TODO consider taking Object and returning String (that way it can solely responsibility of Show)
    static Function1<String, String> script1(Expression expr)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            return v ->
                script2(binaryCall.operator()).apply(
                    script1(binaryCall.left()).apply(v)
                    , script1(binaryCall.right()).apply(v)
                );
        }

        if (expr instanceof Constant constant)
        {
            return ignore -> Show.show(constant.value());
        }

        if (expr instanceof StaticCall staticCall)
        {
            final var head = staticCall.params().head();
            final var method = staticCall.method();
            final Class<?> type = staticCall.type();

            if (head instanceof Hole)
            {
                return script1(method, type);
            }

            return script1(method, type).compose(script1(head));
        }

        if (expr instanceof UnaryCall unaryCall)
        {
            return script1(unaryCall.operator()).compose(script1(unaryCall.expr()));
        }

        throw new RuntimeException("NYI");
    }

    static Function2<String, String, String> script2(Expression expr)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            if (binaryCall.left() instanceof Hole && binaryCall.right() instanceof Hole)
            {
                return script2(binaryCall.operator());
            }

            return script2(binaryCall.left(), binaryCall.operator(), binaryCall.right());
        }

        if (expr instanceof StaticCall staticCall)
        {
            if (staticCall.params().head() instanceof Hole && staticCall.params().tail().head() instanceof Hole)
            {
                return script2(staticCall.method(), staticCall.type());
            }
        }

        if (expr instanceof UnaryCall unaryCall)
        {
            return script2(unaryCall.expr()).andThen(script1(unaryCall.operator()));
        }

        throw new RuntimeException(String.format("NYI: %s", expr));
    }

    private static Function1<String, String> script1(String operator)
    {
        return v -> String.format("%s%s", operator, v);
    }

    private static Function1<String, String> script1(Method method, Class<?> type)
    {
        return v -> String.format(
            "%s.%s(%s)"
            , showClassName(type)
            , method.getName()
            , v
        );
    }

    private static Function2<String, String, String> script2(Method method, Class<?> type)
    {
        return (a, b) -> String.format(
            "%s.%s(%s, %s)"
            , showClassName(type)
            , method.getName()
            , a
            , b
        );
    }

    private static Function2<String, String, String> script2(String operator)
    {
        return (a, b) -> String.format("%s %s %s", a, operator, b);
    }

    private static Function2<String, String, String> script2(Expression left, String operator, Expression right)
    {
        if (left instanceof Constant constant)
        {
            return script2(right).andThen(script2(operator).apply(Show.show(constant.value())));
        }

        if (right instanceof Constant constant)
        {
            return script2(left).andThen(script2(operator).reversed().apply(Show.show(constant.value())));
        }

        return (a, b) ->
            script2(operator).apply(
                script1(left).apply(a)
                , script1(right).apply(b)
            );
    }

    private static String showClassName(Class<?> type)
    {
        final String typeName = type.getName();
        return typeName.startsWith("java.lang.")
            ? List.ofAll(Arrays.stream(typeName.split("\\."))).last()
            : typeName;
    }
}
