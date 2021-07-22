package org.example.ea.samples;

import static org.qbicc.runtime.CNative.*;

public class EASample_02_StaticAssignment
{
    /*
     * This sample showcases the effects of static variable assignment.
     *
     * Objects assigned to static variables are considered to escape.
     * Such objects don't get any optimizations applied.
     * Hence they're allocated as usual in the heap:
     *
     *   %L4 = call i8 addrspace(1)* (i8 addrspace(1)*, i64, i32) @exact.org.qbicc.runtime.gc.nogc.NoGcHelpers.allocate.ref.1.class.java-lang-Object.2.s64.s32(i8 addrspace(1)* %thr0, i64 16, i32 8), !dbg !34 ; EASample_02_StaticAssignment.java:27 bci@0
     *   %L5 = bitcast i8 addrspace(1)* %L4 to %T.org.example.ea.samples.EASample_02_StaticAssignment$A addrspace(1)*, !dbg !34 ; EASample_02_StaticAssignment.java:27 bci@0
     */

    static A sink;

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

        sink = a;

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
