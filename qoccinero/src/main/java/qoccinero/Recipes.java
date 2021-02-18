package qoccinero;

import java.util.function.BiFunction;

// TODO abstract Arbitrary logic to an API that returns a Stream<T>
//      validate that the Stream<T> contains all the edge cases
final class Recipes
{
    static final Recipe.Unary<Double> Double_doubleToLongBits = new Recipe.Unary<>(
        "java_lang_double_doubleToLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToLongBits)
        , v -> String.format("Double.doubleToLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final Recipe.Unary<Double> Double_doubleToRawLongBits = new Recipe.Unary<>(
        "java_lang_double_doubleToRawLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToRawLongBits)
        , v -> String.format("Double.doubleToRawLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final Recipe.Unary<Long> Double_longBitsToDouble = new Recipe.Unary<>(
        "java_lang_double_longBitsToDouble"
        , ParamType.longType().toLiteral()
        , v -> String.format("Double.doubleToRawLongBits(Double.longBitsToDouble(%s))", ParamType.longType().toLiteral().apply(v))
        , ParamType.longType()
    );

    static final Recipe.Unary<Float> Float_floatToRawIntBits = new Recipe.Unary<>(
        "java_lang_float_floatToRawIntBits"
        , ParamType.integerType().toLiteral().compose(Float::floatToRawIntBits)
        , v -> String.format("Float.floatToRawIntBits(%s)", ParamType.floatType().toLiteral().apply(v))
        , ParamType.floatType()
    );

    static final Recipe.Unary<Integer> Float_intBitsToFloat = new Recipe.Unary<>(
        "java_lang_float_intBitsToFloat"
        , ParamType.integerType().toLiteral()
        , v -> String.format("Float.floatToRawIntBits(Float.intBitsToFloat(%s))", ParamType.integerType().toLiteral().apply(v))
        , ParamType.integerType()
    );

    static final Recipe.Binary<Long, Long> Long_divideUnsigned = new Recipe.Binary<>(
        "java_lang_long_divideUnsigned"
        , longDivideUnsigned().andThen(ParamType.longType().toLiteral())
        , (div, by) -> String.format(
            "Long.divideUnsigned(%s, %s)"
            , ParamType.longType().toLiteral().apply(div)
            , ParamType.longType().toLiteral().apply(by)
        )
        , ParamType.longType()
        , ParamType.longType(v -> v != 0)
    );

    private static BiFunction<Long, Long, Long> longDivideUnsigned()
    {
        return Long::divideUnsigned;
    }
}
