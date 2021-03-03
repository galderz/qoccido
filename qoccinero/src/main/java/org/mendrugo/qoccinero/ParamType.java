package org.mendrugo.qoccinero;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Shrinkable;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

sealed interface ParamType<T>
{
    Arbitrary<T> arbitrary();

    // TODO combine literal/hex into a single function

    Function<T, String> toLiteral();

    String toHex(T value);

    Class<?> type();

    static <T> ParamType<T> of(Class<T> type)
    {
        // TODO add byte param type
        return Unchecked.cast(
            switch (type.getName())
            {
                case "double" -> ParamType.doubleType(type);
                case "float" -> ParamType.floatType(type);
                case "int" -> ParamType.integerType(type);
                case "long" -> ParamType.longType(type);
                case "java.lang.Character" -> ParamType.characterType();
                case "java.lang.Double" -> ParamType.doubleType(type);
                case "java.lang.Float" -> ParamType.floatType(type);
                case "java.lang.Integer" -> ParamType.integerType(type);
                case "java.lang.Long" -> ParamType.longType(type);
                case "java.lang.Short" -> ParamType.shortType();
                default -> throw new IllegalStateException("Unexpected value: " + type.getName());
            }
        );
    }

    static ParamType<Boolean> booleanType()
    {
        return new BooleanType(Arbitraries.of(), boolean.class);
    }

    static ParamType<Character> characterType()
    {
        return new CharacterType(Arbitraries.chars());
    }

    static ParamType<Double> doubleType()
    {
        return new DoubleType(Values.doublesOld(), double.class);
    }

    static ParamType<Double> doubleType(Class<?> type)
    {
        return new DoubleType(Values.doublesOld(), type);
    }

    static ParamType<Integer> integerType()
    {
        return new IntegerType(Arbitraries.integers(), int.class);
    }

    static ParamType<Integer> integerType(Class<?> type)
    {
        return new IntegerType(Arbitraries.integers(), type);
    }

    static ParamType<Integer> integerType(Predicate<Integer> filter, Class<?> type)
    {
        return new IntegerType(Values.integers(filter), type);
    }

    static ParamType<Float> floatType()
    {
        return new FloatType(Values.floats(), float.class);
    }

    static ParamType<Float> floatType(Class<?> type)
    {
        return new FloatType(Values.floats(), type);
    }

    static ParamType<Long> longType()
    {
        return new LongType(Values.longs(), long.class);
    }

    static ParamType<Long> longType(Class<?> type)
    {
        return new LongType(Values.longs(), type);
    }

    static ParamType<Long> longType(Predicate<Long> filter, Class<?> type)
    {
        return new LongType(Values.longs(filter), type);
    }

    static ParamType<Short> shortType()
    {
        return new ShortType(Arbitraries.shorts(), short.class);
    }

    static ParamType<Short> shortType(Class<?> type)
    {
        return new ShortType(Arbitraries.shorts(), type);
    }

    final record BooleanType(Arbitrary<Boolean> arbitrary, Class<?> type) implements ParamType<Boolean>
    {
        @Override
        public Function<Boolean, String> toLiteral()
        {
            return String::valueOf;
        }

        @Override
        public String toHex(Boolean value)
        {
            return String.valueOf(value);
        }
    }

    final record CharacterType(Arbitrary<Character> arbitrary) implements ParamType<Character>
    {
        @Override
        public Function<Character, String> toLiteral()
        {
            return ParamType::prettyHex;
        }

        @Override
        public String toHex(Character value)
        {
            return prettyHex(value);
        }

        @Override
        public Class<?> type()
        {
            return null;  // TODO: Customise this generated block
        }
    }

    final record DoubleType(Arbitrary<Double> arbitrary, Class<?> type) implements ParamType<Double>
    {
        @Override
        public Function<Double, String> toLiteral()
        {
            return value ->
            {
                if (Double.isNaN(value))
                    return "Double.NaN";

                if (Double.POSITIVE_INFINITY == value)
                    return "Double.POSITIVE_INFINITY";

                if (Double.NEGATIVE_INFINITY == value)
                    return "Double.NEGATIVE_INFINITY";

                return Double.toString(value);
            };
        }

        @Override
        public String toHex(Double value)
        {
            return prettyHex(Double.doubleToRawLongBits(value));
        }
    }

    final record FloatType(Arbitrary<Float> arbitrary, Class<?> type) implements ParamType<Float>
    {
        @Override
        public Function<Float, String> toLiteral()
        {
            return value ->
            {
                if (Float.isNaN(value))
                    return "Float.NaN";

                if (Float.POSITIVE_INFINITY == value)
                    return "Float.POSITIVE_INFINITY";

                if (Float.NEGATIVE_INFINITY == value)
                    return "Float.NEGATIVE_INFINITY";

                return String.format("%sf", value);
            };
        }

        @Override
        public String toHex(Float value)
        {
            return prettyHex(Float.floatToRawIntBits(value));
        }
    }

    final record IntegerType(Arbitrary<Integer> arbitrary, Class<?> type) implements ParamType<Integer>
    {
        @Override
        public Function<Integer, String> toLiteral()
        {
            return String::valueOf;
        }

        @Override
        public String toHex(Integer value)
        {
            return prettyHex(value);
        }
    }

    final record LongType(Arbitrary<Long> arbitrary, Class<?> type) implements ParamType<Long>
    {
        @Override
        public Arbitrary<Long> arbitrary()
        {
            return arbitrary;
        }

        @Override
        public Function<Long, String> toLiteral()
        {
            return value -> String.format("%sL", value);
        }

        @Override
        public String toHex(Long value)
        {
            return prettyHex(value);
        }
    }

    final record ShortType(Arbitrary<Short> arbitrary, Class<?> type) implements ParamType<Short>
    {
        @Override
        public Function<Short, String> toLiteral()
        {
            return String::valueOf;
        }

        @Override
        public String toHex(Short value)
        {
            return prettyHex(value);
        }
    }

    static <T> Stream<T> values(Arbitrary<T> arbitrary)
    {
        return Stream.concat(
            arbitrary.sampleStream().limit(1000)
            , arbitrary.edgeCases().suppliers().stream()
                .map(Supplier::get)
                .map(Shrinkable::value)
        ).distinct();
    }

//    static <T, U> Stream<Map.Entry<T, U>> values(Arbitrary<T> first, Arbitrary<U> second)
//    {
//        return Arbitraries.entries(first, second).sampleStream().limit(1000);
//    }

    static <T1, T2> List<Tuple2<T1, T2>> values(ParamType<T1> first, ParamType<T2> second)
    {
        return List.ofAll(values(
            Combinators.combine(first.arbitrary(), second.arbitrary())
            .as(Tuple2::new)
        ));
    }

    private static String prettyHex(long l)
    {
        final var hex = Long.toHexString(l).toUpperCase();
        final var padding = "0".repeat(16 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }

    private static String prettyHex(int i)
    {
        final var hex = Integer.toHexString(i).toUpperCase();
        final var padding = "0".repeat(8 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", ""));
    }
}
