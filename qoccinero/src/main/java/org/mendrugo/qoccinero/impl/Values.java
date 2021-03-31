package org.mendrugo.qoccinero.impl;

import io.vavr.Function2;
import io.vavr.collection.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Tuple;

final class Values
{
    static List<List<?>> values(int count, List<Class<?>> types)
    {
        return types.map(
            Function2.of(Values::valuesArbitrary).apply(count)
                .compose(Values::arbitraryForType)
        );
    }

    private static Arbitrary<?> arbitraryForType(Class<?> type)
    {
        return switch (type.getName())
        {
            case "double", "java.lang.Double" -> doubles();
//            case "java.lang.Integer" ->
//                show((Integer) obj, i -> Integer.toString(i), Show::hexInt);
//            case "java.lang.Long" ->
//                show((Long) obj, Show::showLong, Show::hexLong);
            default ->
                throw new RuntimeException("Unexpected type: " + type);
        };
    }

    private static List<?> valuesArbitrary(int count, Arbitrary<?> arbitrary)
    {
        return List.ofAll(arbitrary.sampleStream().limit(count));
    }

    private static Arbitrary<Double> doubles()
    {
        return arbitrary(
            Arbitraries.doubles()
            , List.of(
                Double.NaN                                             // 0x7FF8_0000_0000_0000L
                , Double.NEGATIVE_INFINITY                             // 0xFFF0_0000_0000_0000L
                , Double.MIN_VALUE                                     // 0x0000_0000_0000_0001L
                , -Double.MIN_VALUE                                    // 0x8000_0000_0000_0001L
                , Double.MIN_NORMAL                                    // 0x0010_0000_0000_0000L
                , -Double.MIN_NORMAL                                   // 0x8010_0000_0000_0000L
                , Double.POSITIVE_INFINITY                             // 0x7FF0_0000_0000_0000L
                // , Double.doubleToLongBits(0x7FF8_0000_0000_0100L))  // 0x7FF8_0000_0000_0000L
            )
        );
    }

    private static <T> Arbitrary<T> arbitrary(Arbitrary<T> arbitrary, List<T> edgeCases)
    {
        return Arbitraries.frequencyOf(
            Tuple.of(15, arbitrary),
            Tuple.of(1, Arbitraries.of(edgeCases.asJava()))
        );
    }
}
