package org.example.ea.samples;

import static org.qbicc.runtime.CNative.*;

public class EASample_01_Basic
{

    /*
     * This sample showcases basic escape analysis.
     *
     * It consists of a single method that creates an object,
     * but its lifespan does not escape the method itself.
     *
     * Escape analysis should detect,
     * that and apply corresponding optimizations.
     * E.g. stack allocate the object:
     *
     *   %L8 = alloca %T.org.example.ea.samples.EASample_01_Basic$A, i32 1, align 8
     *
     * For reference, an object that is not stack allocated would look like:
     *
     *   %L8 = call i8 addrspace(1)* (i8 addrspace(1)*, i64, i32) @exact.org.qbicc.runtime.gc.nogc.NoGcHelpers.allocate.ref.1.class.java-lang-Object.2.s64.s32(i8 addrspace(1)* %thr0, i64 16, i32 8), !dbg !35 ; EASample_01_Basic.java:33 bci@0
     *   %L9 = bitcast i8 addrspace(1)* %L8 to %T.org.example.ea.samples.EASample_01_Basic$A addrspace(1)*, !dbg !35 ; EASample_01_Basic.java:33 bci@0
     */

    public static void main(String[] args)
    {
        putchar(sample(10, 20, 30) == 10 ? '.' : 'F');
    }

    static int sample(int one, int two, int three)
    {
        A a = new A();
        a.one = one;
        a.two = two;
        a.three = three;
        return a.one;
    }

    public static class A
    {
        int one;
        int two;
        int three;
    }

    @extern
    public static native int putchar(int arg);
}
