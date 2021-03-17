package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.Function2;

import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Show
{
    static String show(Object obj)
    {
        return switch (obj.getClass().getName())
        {
            case "java.lang.Double" ->
                show()
                    .apply(show((Double) obj))
                    .apply(Function1.of(Show::prettyHex).compose(Double::doubleToRawLongBits).apply((Double) obj));
            case "java.lang.Long" ->
                show()
                    .apply(show((Long) obj))
                    .apply(prettyHex((Long) obj));
            default ->
                throw new RuntimeException("Unexpected type: " + obj.getClass());
        };
    }

    private static String show(Long l)
    {
        return String.format("%sL", l);
    }

    private static Function2<String, String, String> show()
    {
        return (v, hex) -> String.format("%s /* %s */", v, hex);
    }

    private static String show(Double d)
    {
        if (Double.isNaN(d))
            return "Double.NaN";

        if (d.equals(Double.POSITIVE_INFINITY))
            return "Double.POSITIVE_INFINITY";

        if (d.equals(Double.NEGATIVE_INFINITY))
            return "Double.NEGATIVE_INFINITY";

        return Double.toString(d);
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
}
