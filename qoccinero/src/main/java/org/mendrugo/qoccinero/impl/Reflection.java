package org.mendrugo.qoccinero.impl;

import io.vavr.CheckedFunction2;
import org.mendrugo.qoccinero.Unchecked;

import java.lang.reflect.Method;

public class Reflection
{
    static <T1, R> R invoke(T1 v, Method method, Class<?> type)
    {
        return Unchecked.cast(
            CheckedFunction2.<Object, Object[], Object>of(method::invoke)
                .unchecked()
                .apply(type, new Object[]{v})
        );
    }
}
