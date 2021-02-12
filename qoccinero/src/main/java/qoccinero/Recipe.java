package qoccinero;

import java.util.function.Function;

final class Recipe<T>
{
    final String name;
    final Function<T, String> expected;
    final Function<T, String> function;
    final ParamType<T> paramType;

    Recipe(String name, Function<T, String> expected, Function<T, String> function, ParamType<T> paramType) {
        this.name = name;
        this.expected = expected;
        this.function = function;
        this.paramType = paramType;
    }


//    final Function<T, R> function;
//    final String name;
//    final ParamType<R> returnType;
//    final ParamType<T> paramType;
//
//    Recipe(Function<T, R> function, String name, ParamType<R> returnType, ParamType<T> paramType) {
//        this.function = function;
//        this.name = name;
//        this.returnType = returnType;
//        this.paramType = paramType;
//    }

//    String className()
//    {
//        return name.replace(".", "_");
//    }
}
