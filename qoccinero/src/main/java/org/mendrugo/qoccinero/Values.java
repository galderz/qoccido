package org.mendrugo.qoccinero;

import io.vavr.Tuple2;
import io.vavr.collection.List;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.Combinators;
import net.jqwik.api.Shrinkable;
import net.jqwik.api.Tuple;

import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Values
{
    static <T1, T2> List<Tuple2<T1, T2>> values(Arbitrary<T1> first, Arbitrary<T2> second)
    {
        return Values
            .values(Combinators.combine(first, second).as(Tuple2::new))
            .distinct();
    }

    static <T> List<T> values(Arbitrary<T> arbitrary)
    {
        return List.ofAll(arbitrary.sampleStream().limit(1000));
    }

    static Arbitrary<Double> doubles()
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

    static Arbitrary<Float> floats()
    {
        // Already included:
        //  MAX_VALUE 0x7F7F_FFFFL
        // -MAX_VALUE 0xFF7F_FFFFL
        return Arbitraries.floats()
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
    }

    static Arbitrary<Integer> integers()
    {
        // TODO double check edge cases
        return Arbitraries.integers();
    }

    static Arbitrary<Integer> integers(Predicate<Integer> filter)
    {
        // TODO double check edge cases
        return Arbitraries.integers().filter(filter);
    }

    static Arbitrary<Long> longs()
    {
        // TODO double check edge cases
        return Arbitraries.longs();
    }

    static Arbitrary<Long> longs(Predicate<Long> filter)
    {
        // TODO double check edge cases
        return Arbitraries.longs().filter(filter);
    }

    @Deprecated
    private static <T> List<T> valuesOld(Arbitrary<T> arbitrary)
    {
        return List.ofAll(
            Stream
                .concat(
                    arbitrary.sampleStream().limit(1000)
                    , arbitrary.edgeCases().suppliers().stream()
                        .map(Supplier::get)
                        .map(Shrinkable::value))
                .distinct()
        );
    }

//    private static <T> List<T> values(Arbitrary<T> arbitrary, List<T> edgeCases)
//    {
//        return List.ofAll(
//            arbitrary(arbitrary, edgeCases)
//                .sampleStream().limit(1000)
//        );
//    }

    private static <T> Arbitrary<T> arbitrary(Arbitrary<T> arbitrary, List<T> edgeCases)
    {
        return Arbitraries.frequencyOf(
            Tuple.of(20, arbitrary),
            Tuple.of(1, Arbitraries.of(edgeCases.asJava()))
        );
    }

//    private static <T> List<T> values(Arbitrary<T> arbitrary, List<T> edgeCases)
//    {
//        return List.ofAll(arbitrary.sampleStream().limit(1000))
//            .appendAll(edgeCases)
//            .distinct();
//
////        return List.ofAll(
////            Stream
////                .concat(
////                    arbitrary.sampleStream().limit(1000)
////                    , arbitrary.edgeCases().suppliers().stream()
////                        .map(Supplier::get)
////                        .map(Shrinkable::value))
////                .distinct()
////        );
//    }
}
