package qoccinero;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Recetas
{
    static final Receta<Double, Long> Double_doubleToRawLongBits = new Receta<>(
        Recetas::doubles
        , Recetas::doubleLiteral
        , Double::doubleToRawLongBits
        , Recetas::prettyHexDoubleToRawLongBits
        , "Double.doubleToRawLongBits"
    );

    static Arbitrary<Double> doubles()
    {
        // MAX_VALUE and -MAX_VALUE included
        return Arbitraries.doubles()
            .edgeCases(edgeCasesConfig ->
                edgeCasesConfig
                    .add(Double.NaN)                                      // 0x7FF8_0000_0000_0000L
                    .add(Double.NEGATIVE_INFINITY)                        // 0xFFF0_0000_0000_0000L
                    .add(Double.MIN_VALUE)                                // 0x0000_0000_0000_0001L
                    .add(-Double.MIN_VALUE)                               // 0x8000_0000_0000_0001L
                    .add(Double.MIN_NORMAL)                               // 0x0010_0000_0000_0000L
                    .add(-Double.MIN_NORMAL)                              // 0x8010_0000_0000_0000L
                    .add(Double.POSITIVE_INFINITY)                        // 0x7FF0_0000_0000_0000L
                    //.add(Double.longBitsToDouble(0x7FF8_0000_0000_0100L)) // 0x7FF8_0000_0000_0000L
        );
    }

    static String doubleLiteral(double aDouble)
    {
        if (Double.isNaN(aDouble))
            return "Double.NaN";

        if (Double.POSITIVE_INFINITY == aDouble)
            return "Double.POSITIVE_INFINITY";

        if (Double.NEGATIVE_INFINITY == aDouble)
            return "Double.NEGATIVE_INFINITY";

        return Double.toString(aDouble);
    }

    static String prettyHexDoubleToLongBits(double d)
    {
        return prettyHex(Double.doubleToLongBits(d));
    }

    static String prettyHexDoubleToRawLongBits(double d)
    {
        return prettyHex(Double.doubleToRawLongBits(d));
    }

    static String prettyHex(long l)
    {
        final var hex = Long.toHexString(l).toUpperCase();
        final var padding = "0".repeat(16 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }
}
