package org.mendrugo.qoccinero;

import java.util.function.BiFunction;
import java.util.function.Function;

sealed interface RecipeOld
{
    final record Unary<T>(
        String name
        , Function<T, String> expected
        , Function<T, String> function
        , ParamType<T> paramType
    ) implements RecipeOld {}

    final record Binary<T, U>(
        String name
        , BiFunction<T, U, String> expected
        , BiFunction<T, U, String> function
        , ParamType<T> firstType
        , ParamType<U> secondType
    ) implements RecipeOld {}
}
