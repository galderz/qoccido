package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;

import java.lang.reflect.Method;
import java.util.Arrays;

public class Scripts
{
    static Function1<String, String> script1(Expression expr)
    {
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

            //return function2(binaryCall.left(), binaryCall.operator(), binaryCall.right());
        }

        throw new RuntimeException(String.format("NYI: %s", expr));
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

    private static Function2<String, String, String> script2(String operator)
    {
        return (a, b) -> String.format("%s %s %s", a, operator, b);
    }

    private static String showClassName(Class<?> type)
    {
        final String typeName = type.getName();
        return typeName.startsWith("java.lang.")
            ? List.ofAll(Arrays.stream(typeName.split("\\."))).last()
            : typeName;
    }
}
