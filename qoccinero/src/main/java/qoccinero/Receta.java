package qoccinero;

import net.jqwik.api.Arbitrary;

import java.util.function.Function;
import java.util.function.Supplier;

final class Receta<T, R>
{
    final Supplier<Arbitrary<T>> arbitrary;
    final Function<T, String> toLiteral;
    final Function<T, R> function;
    final Function<T, String> comment;
    final String name;

    Receta(Supplier<Arbitrary<T>> arbitrary, Function<T, String> toLiteral, Function<T, R> function, Function<T, String> comment, String name) {
        this.arbitrary = arbitrary;
        this.toLiteral = toLiteral;
        this.function = function;
        this.comment = comment;
        this.name = name;
    }

    String className()
    {
        return name.replace(".", "_");
    }
}
