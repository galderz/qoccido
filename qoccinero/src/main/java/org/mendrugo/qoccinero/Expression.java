package org.mendrugo.qoccinero;

import io.vavr.Tuple2;

sealed interface Expression<T> permits Literal, StaticMethod
{
    String id();

    ParamType<T> returns();

    Expects<T> expects();

    static <T> Expression<T> of(Recipe recipe)
    {
        if (recipe instanceof Recipe.Type t)
        {
            return Literal.of(Unchecked.cast(ParamType.of(t.type())));
        }

        return null;
    }
}
