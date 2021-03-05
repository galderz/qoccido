package org.mendrugo.qoccinero;

import io.vavr.Tuple2;

sealed interface Expression<T> permits Literal
{
    String id();

    ParamType<T> returns();

    Expects<T> expects();
}
