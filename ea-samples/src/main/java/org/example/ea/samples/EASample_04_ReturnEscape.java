package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_04_ReturnEscape
{
    /**
     * This sample showcases the effects of new objects that escape via objects returned by methods.
     * 
     * The below sample2 method instantiates an A instance,
     * but this is returned to the caller,
     * so it can't be stack allocated:
     *
     *   define i8 addrspace(1)* @exact.org.example.ea.samples.EASample_04_ReturnEscape.sample2.ref.1.class.org-example-ea-samples-EASample_04_ReturnEscape$A.0(i8 addrspace(1)* %thr0) "frame-pointer"="non-leaf" uwtable gc "statepoint-example" !dbg !45 {
     *       %L4 = call i8 addrspace(1)* (i8 addrspace(1)*, i64, i32) @exact.org.qbicc.runtime.gc.nogc.NoGcHelpers.allocate.ref.1.class.java-lang-Object.2.s64.s32(i8 addrspace(1)* %thr0, i64 8, i32 8), !dbg !46 ; EASample_04_ReturnEscape.java:xx bci@0
     *       %L5 = bitcast i8 addrspace(1)* %L4 to %T.org.example.ea.samples.EASample_04_ReturnEscape$A addrspace(1)*, !dbg !46 ; EASample_04_ReturnEscape.java:xx bci@0
     */

    public static void main(String[] args)
    {
        Main.print(sample() == 10 ? '.' : 'F');
    }

    static int sample()
    {
        A result = sample2();
        return result.aField;
    }

    static A sample2()
    {
        A a = new A();
        a.aField = 10;
        return a;
    }

    public static class A
    {
        int aField;
    }
}
