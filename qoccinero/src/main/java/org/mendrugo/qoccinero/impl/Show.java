package org.mendrugo.qoccinero.impl;

import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Show
{
    static String show(Object obj)
    {
        return switch (obj.getClass().getName())
        {
            case "java.lang.Double" ->
                show(showDouble(obj), hexDouble(obj));
            case "java.lang.Long" ->
                show(showLong(obj), prettyHex((Long) obj));
            default ->
                throw new RuntimeException("Unexpected type: " + obj.getClass());
        };
    }

    private static String showLong(Object obj)
    {
        return String.format("%sL", obj);
    }

    private static String show(Object value, Object hex)
    {
        return String.format("%s /* %s */", value, hex);
    }

    private static String showDouble(Object obj)
    {
        Double d = (Double) obj;

        if (Double.isNaN(d))
            return "Double.NaN";

        if (obj.equals(Double.POSITIVE_INFINITY))
            return "Double.POSITIVE_INFINITY";

        if (obj.equals(Double.NEGATIVE_INFINITY))
            return "Double.NEGATIVE_INFINITY";

        return Double.toString(d);
    }

    private static String hexDouble(Object obj)
    {
        return prettyHex(Double.doubleToRawLongBits((Double) obj));
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
