package qoccinero;

import java.util.function.BiFunction;
import java.util.function.Function;

sealed interface Recipe
{
    final record Unary<T>(
        String name
        , Function<T, String> expected
        , Function<T, String> function
        , ParamType<T> paramType
    ) implements Recipe {}

    final record Binary<T, U>(
        String name
        , BiFunction<T, U, String> expected
        , BiFunction<T, U, String> function
        , ParamType<T> firstType
        , ParamType<U> secondType
    ) implements Recipe {}
}
