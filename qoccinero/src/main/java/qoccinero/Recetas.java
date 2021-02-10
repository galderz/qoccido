package qoccinero;

// TODO abstract Arbitrary logic to an API that returns a Stream<T>
//      validate that the Stream<T> contains all the edge cases
final class Recetas
{
    static final Receta<Double, Long> Double_doubleToRawLongBits = new Receta<>(
        Double::doubleToRawLongBits
        , "Double.doubleToRawLongBits"
        , ParamType.doubleType()
    );

    static final Receta<Float, Integer> Float_floatToRawIntBits = new Receta<>(
        Float::floatToRawIntBits
        , "Float.floatToRawIntBits"
        , ParamType.floatType()
    );
}
