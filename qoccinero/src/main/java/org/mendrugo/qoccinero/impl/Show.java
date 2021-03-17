package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;

import java.util.stream.Collectors;
import java.util.stream.Stream;

final class Show
{
    static String show(Object obj)
    {
        return switch (obj.getClass().getName())
        {
            case "java.lang.Double" ->
                show((Double) obj, Show::showDouble, Function1.of(Show::hexLong).compose(Double::doubleToRawLongBits));
            case "java.lang.Integer" ->
                show((Integer) obj, i -> Integer.toString(i), Show::hexInt);
            case "java.lang.Long" ->
                show((Long) obj, Show::showLong, Show::hexLong);
            default ->
                throw new RuntimeException("Unexpected type: " + obj.getClass());
        };
    }

    private static <T> String show(T obj, Function1<T, String> toLiteral, Function1<T, String> toHex)
    {
        return String.format(
            "%s /* %s */"
            , toLiteral.apply(obj)
            , toHex.apply(obj)
        );
    }

    private static String showDouble(Double d)
    {
        if (Double.isNaN(d))
            return "Double.NaN";

        if (d.equals(Double.POSITIVE_INFINITY))
            return "Double.POSITIVE_INFINITY";

        if (d.equals(Double.NEGATIVE_INFINITY))
            return "Double.NEGATIVE_INFINITY";

        return Double.toString(d);
    }

    private static String showLong(Long l)
    {
        return String.format("%sL", l);
    }

    private static String hexInt(int i)
    {
        final var hex = Integer.toHexString(i).toUpperCase();
        final var padding = "0".repeat(8 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", ""));
    }

    private static String hexLong(long l)
    {
        final var hex = Long.toHexString(l).toUpperCase();
        final var padding = "0".repeat(16 - hex.length());
        final var paddedHex = padding + hex;
        return Stream
            .of(paddedHex.split("(?<=\\G.{4})"))
            .collect(Collectors.joining("_", "0x", "L"));
    }
}
