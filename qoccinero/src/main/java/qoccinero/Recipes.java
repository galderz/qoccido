package qoccinero;

import java.util.function.BiFunction;

// TODO abstract Arbitrary logic to an API that returns a Stream<T>
//      validate that the Stream<T> contains all the edge cases
final class Recipes
{
    static final RecipeOld.Binary<Character, Character> Character_compare = new RecipeOld.Binary<>(
        "java_lang_Character_compare"
        , characterCompare().andThen(ParamType.integerType().toLiteral())
        , (v1, v2) -> String.format(
            "Character.compare((char) %s, (char) %s)"
        , ParamType.characterType().toLiteral().apply(v1)
            , ParamType.characterType().toLiteral().apply(v2)
        )
        , ParamType.characterType()
        , ParamType.characterType()
    );

    static final RecipeOld.Unary<Double> Double_doubleToLongBits = new RecipeOld.Unary<>(
        "java_lang_Double_doubleToLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToLongBits)
        , v -> String.format("Double.doubleToLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final RecipeOld.Unary<Double> Double_doubleToRawLongBits = new RecipeOld.Unary<>(
        "java_lang_Double_doubleToRawLongBits"
        , ParamType.longType().toLiteral().compose(Double::doubleToRawLongBits)
        , v -> String.format("Double.doubleToRawLongBits(%s)", ParamType.doubleType().toLiteral().apply(v))
        , ParamType.doubleType()
    );

    static final RecipeOld.Unary<Long> Double_longBitsToDouble = new RecipeOld.Unary<>(
        "java_lang_Double_longBitsToDouble"
        , ParamType.longType().toLiteral()
        , v -> String.format("Double.doubleToRawLongBits(Double.longBitsToDouble(%s))", ParamType.longType().toLiteral().apply(v))
        , ParamType.longType()
    );

    static final RecipeOld.Unary<Float> Float_floatToRawIntBits = new RecipeOld.Unary<>(
        "java_lang_Float_floatToRawIntBits"
        , ParamType.integerType().toLiteral().compose(Float::floatToRawIntBits)
        , v -> String.format("Float.floatToRawIntBits(%s)", ParamType.floatType().toLiteral().apply(v))
        , ParamType.floatType()
    );

    static final RecipeOld.Unary<Integer> Float_intBitsToFloat = new RecipeOld.Unary<>(
        "java_lang_Float_intBitsToFloat"
        , ParamType.integerType().toLiteral()
        , v -> String.format("Float.floatToRawIntBits(Float.intBitsToFloat(%s))", ParamType.integerType().toLiteral().apply(v))
        , ParamType.integerType()
    );

    static final RecipeOld.Binary<Integer, Integer> Integer_compare = new RecipeOld.Binary<>(
        "java_lang_Integer_compare"
        , integerCompare().andThen(ParamType.integerType().toLiteral())
        , (v1, v2) -> String.format(
            "Integer.compare(%s, %s)"
            , ParamType.integerType().toLiteral().apply(v1)
            , ParamType.integerType().toLiteral().apply(v2)
        )
        , ParamType.integerType()
        , ParamType.integerType()
    );

    static final RecipeOld.Binary<Integer, Integer> Integer_compareUnsigned = new RecipeOld.Binary<>(
        "java_lang_Integer_compareUnsigned"
        , integerCompareUnsigned().andThen(ParamType.integerType().toLiteral())
        , (v1, v2) -> String.format(
            "Integer.compareUnsigned(%s, %s)"
            , ParamType.integerType().toLiteral().apply(v1)
            , ParamType.integerType().toLiteral().apply(v2)
        )
        , ParamType.integerType()
        , ParamType.integerType()
    );

    static final RecipeOld.Binary<Integer, Integer> Integer_divideUnsigned = new RecipeOld.Binary<>(
        "java_lang_Integer_divideUnsigned"
        , integerDivideUnsigned().andThen(ParamType.integerType().toLiteral())
        , (div, by) -> String.format(
            "Integer.divideUnsigned(%s, %s)"
            , ParamType.integerType().toLiteral().apply(div)
            , ParamType.integerType().toLiteral().apply(by)
        )
        , ParamType.integerType()
        , ParamType.integerType(v -> v != 0, int.class)
    );

    static final RecipeOld.Binary<Integer, Integer> Integer_remainderUnsigned = new RecipeOld.Binary<>(
        "java_lang_Integer_remainderUnsigned"
        , integerRemainderUnsigned().andThen(ParamType.integerType().toLiteral())
        , (div, by) -> String.format(
            "Integer.remainderUnsigned(%s, %s)"
            , ParamType.integerType().toLiteral().apply(div)
            , ParamType.integerType().toLiteral().apply(by)
        )
        , ParamType.integerType()
        , ParamType.integerType(v -> v != 0, int.class)
    );

    static final RecipeOld.Binary<Long, Long> Long_divideUnsigned = new RecipeOld.Binary<>(
        "java_lang_Long_divideUnsigned"
        , longDivideUnsigned().andThen(ParamType.longType().toLiteral())
        , (div, by) -> String.format(
            "Long.divideUnsigned(%s, %s)"
            , ParamType.longType().toLiteral().apply(div)
            , ParamType.longType().toLiteral().apply(by)
        )
        , ParamType.longType()
        , ParamType.longType(v -> v != 0, long.class)
    );

    static final RecipeOld.Binary<Long, Long> Long_remainderUnsigned = new RecipeOld.Binary<>(
        "java_lang_Long_remainderUnsigned"
        , longRemainderUnsigned().andThen(ParamType.longType().toLiteral())
        , (div, by) -> String.format(
            "Long.remainderUnsigned(%s, %s)"
            , ParamType.longType().toLiteral().apply(div)
            , ParamType.longType().toLiteral().apply(by)
        )
        , ParamType.longType()
        , ParamType.longType(v -> v != 0, long.class)
    );

    static final RecipeOld.Binary<Short, Short> Short_compare = new RecipeOld.Binary<>(
        "java_lang_Short_compare"
        , shortCompare().andThen(ParamType.integerType().toLiteral())
        , (v1, v2) -> String.format(
            "Short.compare((short) %s, (short) %s)"
            , ParamType.shortType().toLiteral().apply(v1)
            , ParamType.shortType().toLiteral().apply(v2)
        )
        , ParamType.shortType()
        , ParamType.shortType()
    );

    static final RecipeOld.Binary<Short, Short> Short_compareUnsigned = new RecipeOld.Binary<>(
        "java_lang_Short_compareUnsigned"
        , shortCompareUnsigned().andThen(ParamType.integerType().toLiteral())
        , (v1, v2) -> String.format(
            "Short.compareUnsigned((short) %s, (short) %s)"
            , ParamType.shortType().toLiteral().apply(v1)
            , ParamType.shortType().toLiteral().apply(v2)
        )
        , ParamType.shortType()
        , ParamType.shortType()
    );

    private static BiFunction<Character, Character, Integer> characterCompare()
    {
        return Character::compare;
    }

    private static BiFunction<Integer, Integer, Integer> integerCompare()
    {
        return Integer::compare;
    }

    private static BiFunction<Integer, Integer, Integer> integerCompareUnsigned()
    {
        return Integer::compareUnsigned;
    }

    private static BiFunction<Integer, Integer, Integer> integerDivideUnsigned()
    {
        return Integer::divideUnsigned;
    }

    private static BiFunction<Integer, Integer, Integer> integerRemainderUnsigned()
    {
        return Integer::remainderUnsigned;
    }

    private static BiFunction<Long, Long, Long> longDivideUnsigned()
    {
        return Long::divideUnsigned;
    }

    private static BiFunction<Long, Long, Long> longRemainderUnsigned()
    {
        return Long::remainderUnsigned;
    }

    private static BiFunction<Short, Short, Integer> shortCompare()
    {
        // Compare short using Integer.compare for alignment
        return Integer::compare;
    }

    private static BiFunction<Short, Short, Integer> shortCompareUnsigned()
    {
        // Compare short using Integer.compare for alignment
        return Integer::compareUnsigned;
    }
}
