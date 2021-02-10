package qoccinero;

import java.util.function.Function;

final class Receta<T, R>
{
//    final Arbitrary<T> arbitrary;
//    final Function<T, String> toLiteral;
    final Function<T, R> function;
//    final Function<T, String> comment;
    final String name;
    final ParamType<T> paramType;

    Receta(Function<T, R> function, String name, ParamType<T> paramType) {
        this.function = function;
        this.name = name;
        this.paramType = paramType;
    }

//    Receta(Arbitrary<T> arbitrary, Function<T, String> toLiteral, Function<T, R> function, Function<T, String> comment, String name) {
//        this.arbitrary = arbitrary;
//        this.toLiteral = toLiteral;
//        this.function = function;
//        this.comment = comment;
//        this.name = name;
//    }

    String className()
    {
        return name.replace(".", "_");
    }
}
