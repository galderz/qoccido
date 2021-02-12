package qoccinero;

import java.util.function.Function;

// TODO abstract Arbitrary logic to an API that returns a Stream<T>
//      validate that the Stream<T> contains all the edge cases
final class Recipes
{
    static final Recipe<Double> Double_doubleToLongBits = new Recipe<>(
        "Double_doubleToLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToLongBits)
        , v -> String.format("Double.doubleToLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final Recipe<Double> Double_doubleToRawLongBits = new Recipe<>(
        "Double_doubleToRawLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToRawLongBits)
        , v -> String.format("Double.doubleToRawLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final Recipe<Long> Double_longBitsToDouble = new Recipe<>(
        "Double_longBitsToDouble"
        , ParamType.longType().toLiteral()
        , v -> String.format("Double.doubleToRawLongBits(Double.longBitsToDouble(%s))", ParamType.longType().toLiteral().apply(v))
        , ParamType.longType()
    );

    static final Recipe<Float> Float_floatToRawIntBits = new Recipe<>(
        "Float_floatToRawIntBits"
        , ParamType.integerType().toLiteral().compose(Float::floatToRawIntBits)
        , v -> String.format("Float.floatToRawIntBits(%s)", ParamType.floatType().toLiteral().apply(v))
        , ParamType.floatType()
    );

    static final Recipe<Integer> Float_intBitsToFloat = new Recipe<>(
        "Float_intBitsToFloat"
        , ParamType.integerType().toLiteral()
        , v -> String.format("Float.floatToRawIntBits(Float.intBitsToFloat(%s))", ParamType.integerType().toLiteral().apply(v))
        , ParamType.integerType()
    );
}
