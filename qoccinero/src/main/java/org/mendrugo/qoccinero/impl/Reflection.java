package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import io.vavr.Function1;
import io.vavr.Function2;

import java.lang.reflect.Method;

public class Reflection
{
    static Function1<Object, Object> invoke1(Method method, Class<?> type)
    {
        return v ->
            CheckedFunction2.<Object, Object[], Object>of(method::invoke)
                .unchecked()
                .apply(type, new Object[]{v});
    }

    static Function2<Object, Object, Object> invoke2(Method method, Class<?> type)
    {
        return (a, b) ->
            CheckedFunction2.<Object, Object[], Object>of(method::invoke)
                .unchecked()
                .apply(type, new Object[]{a, b});
    }
}
