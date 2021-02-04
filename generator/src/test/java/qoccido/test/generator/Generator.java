package qoccido.test.generator;

import net.jqwik.api.Arbitraries;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;

public class Generator extends AbstractGenerator
{
    int index = 0;

    public Generator()
    {
        super("Double_doubleToLongBits");
    }

    @Provide
    Arbitrary<Double> allDoubles()
    {
        // MAX_VALUE and -MAX_VALUE included
        return Arbitraries.doubles().edgeCases(edgeCasesConfig ->
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

    @Property
    boolean allDoubles(@ForAll("allDoubles") double aDouble)
    {
        String doubleString = show(aDouble);
        methodBuilder.addCode(
            "putchar($Ll == Double.doubleToLongBits($L) ? '.' : 'F'); // $L\n"
            , Double.doubleToLongBits(aDouble)
            , doubleString
            , DoubleHex.doubleToLongBits(aDouble)
        );

        if (++index % 80 == 0)
        {
            methodBuilder.addStatement("putchar('\\n')");
        }

        return true;
    }

    private String show(double aDouble)
    {
        if (Double.isNaN(aDouble))
            return "Double.NaN";

        if (Double.POSITIVE_INFINITY == aDouble)
            return "Double.POSITIVE_INFINITY";

        if (Double.NEGATIVE_INFINITY == aDouble)
            return "Double.NEGATIVE_INFINITY";

        return Double.toString(aDouble);
    }
}
