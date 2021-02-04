package qoccido.test.generator;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DoubleHex
{
    static String doubleToLongBits(double d)
    {
        return prettyHex(Double.doubleToLongBits(d));
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
