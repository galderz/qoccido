package qoccinero;

import java.util.function.Function;

final class OneWayRecipe<T, R>
{
    final Function<T, R> function;
    final String name;
    final ParamType<R> returnType;
    final ParamType<T> paramType;

    OneWayRecipe(Function<T, R> function, String name, ParamType<R> returnType, ParamType<T> paramType) {
        this.function = function;
        this.name = name;
        this.returnType = returnType;
        this.paramType = paramType;
    }

    String className()
    {
        return name.replace(".", "_");
    }
}
