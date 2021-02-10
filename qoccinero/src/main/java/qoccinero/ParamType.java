package qoccinero;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.stream.Collectors;
import java.util.stream.Stream;

interface ParamType<T>
{
    Arbitrary<T> arbitrary();

    String asLiteral(T value);

    String asReadable(T value);

    static ParamType<Double> doubleType()
    {
        return new DoubleType();
    }

    static ParamType<Float> floatType()
    {
        return new FloatType();
    }

    final class DoubleType implements ParamType<Double>
    {
        // Already included:
        //  MAX_VALUE  0x7FEF_FFFF_FFFF_FFFFL
        // -MAX_VALUE  0xFFEF_FFFF_FFFF_FFFFL
        private final Arbitrary<Double> arbitrary =
            Arbitraries.doubles()
                .edgeCases(edgeCasesConfig ->
                        edgeCasesConfig
                            .add(Double.NaN)                                      // 0x7FF8_0000_0000_0000L
                            .add(Double.NEGATIVE_INFINITY)                        // 0xFFF0_0000_0000_0000L
                            .add(Double.MIN_VALUE)                                // 0x0000_0000_0000_0001L
                            .add(-Double.MIN_VALUE)                               // 0x8000_0000_0000_0001L
                            .add(Double.MIN_NORMAL)                               // 0x0010_0000_0000_0000L
                            .add(-Double.MIN_NORMAL)                              // 0x8010_0000_0000_0000L
                            .add(Double.POSITIVE_INFINITY)                        // 0x7FF0_0000_0000_0000L
                            //.add(Double.doubleToLongBits(0x7FF8_0000_0000_0100L))  // 0x7FF8_0000_0000_0000L
                );

        @Override
        public Arbitrary<Double> arbitrary()
        {
            return arbitrary;
        }

        @Override
        public String asLiteral(Double value)
        {
            if (Double.isNaN(value))
                return "Double.NaN";

            if (Double.POSITIVE_INFINITY == value)
                return "Double.POSITIVE_INFINITY";

            if (Double.NEGATIVE_INFINITY == value)
                return "Double.NEGATIVE_INFINITY";

            return Double.toString(value);
        }

        @Override
        public String asReadable(Double value)
        {
            return prettyHex(Double.doubleToRawLongBits(value));
        }
    }

    final class FloatType implements ParamType<Float>
    {
        // Already included:
        //  MAX_VALUE 0x7F7F_FFFFL
        // -MAX_VALUE 0xFF7F_FFFFL
        private final Arbitrary<Float> arbitrary =
            Arbitraries.floats()
                .edgeCases(edgeCasesConfig ->
                        edgeCasesConfig
                            .add(Float.NaN)                                // 0x7FC0_0000L
                            .add(Float.NEGATIVE_INFINITY)                  // 0xFF80_0000L
                            .add(Float.MIN_VALUE)                          // 0x0000_0001L
                            .add(-Float.MIN_VALUE)                         // 0x8000_0001L
                            .add(Float.MIN_NORMAL)                         // 0x0080_0000L
                            .add(-Float.MIN_NORMAL)                        // 0x8080_0000L
                            .add(Float.POSITIVE_INFINITY)                  // 0x7F80_0000L
                            //.add(Float.floatToIntBits(0x7FC0_0100L))     // 0x7FC0_0000L
                );

        @Override
        public Arbitrary<Float> arbitrary()
        {
            return arbitrary;
        }

        @Override
        public String asLiteral(Float value)
        {
            if (Float.isNaN(value))
                return "Float.NaN";

            if (Float.POSITIVE_INFINITY == value)
                return "Float.POSITIVE_INFINITY";

            if (Float.NEGATIVE_INFINITY == value)
                return "Float.NEGATIVE_INFINITY";

            return String.format("%sf", value);
        }

        @Override
        public String asReadable(Float value)
        {
            return prettyHex(Float.floatToRawIntBits(value));
        }

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

    static String prettyHex(int i)
    {
        final var hex = Integer.toHexString(i).toUpperCase();
        final var padding = "0".repeat(8 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }
}
