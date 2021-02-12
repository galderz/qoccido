import cc.quarkus.qcc.runtime.stdc.Stdint.uint32_t;

import static cc.quarkus.qcc.runtime.CNative.*;

public class example
{
    @extern
    public static native int putchar(c_int arg);

    @export
    public static c_int main(c_int argc, ptr<?> argv)
    {
        bitCastOptimizationTest();
        // smokeTest();

        return word(0);
    }

    // Exercises this optimization:
    // BitCast(BitCast(a, x), y) -> BitCast(a, y)
    private static void bitCastOptimizationTest()
    {
        final uint32_t foo = word(1234);
        final float bar = Float.intBitsToFloat(foo.intValue());
        putchar(1.729E-42f == bar ? word('.') : word('F'));
    }

    private static void smokeTest()
    {
        _Float32 foo = word(1.0f);
        _Float32 bar = word(1.0f);
        putchar(foo == bar ? word('.') : word('F'));
    }

    public static void main(String[] args)
    {
        // make driver happy
    }
}
