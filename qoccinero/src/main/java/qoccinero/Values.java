package qoccinero;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;

import java.util.function.Predicate;

public class Values
{
    static Arbitrary<Double> doubles()
    {
        // Already included:
        //  MAX_VALUE  0x7FEF_FFFF_FFFF_FFFFL
        // -MAX_VALUE  0xFFEF_FFFF_FFFF_FFFFL
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
                    //.add(Double.doubleToLongBits(0x7FF8_0000_0000_0100L))  // 0x7FF8_0000_0000_0000L
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
}
