package org.mendrugo.qoccinero;

import io.vavr.Tuple2;

sealed interface Expression<T> permits Literal
{
    String id();

    ParamType<T> returns();

    // TODO convert Tuple2 into something more meaninful (e.g. Asserts)
    Tuple2<T, String> asserts();
}
