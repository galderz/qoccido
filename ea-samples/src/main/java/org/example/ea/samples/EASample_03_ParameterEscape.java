package org.example.ea.samples;

import static org.qbicc.runtime.CNative.*;

public class EASample_03_ParameterEscape
{
    /**
     * This sample showcases the effects of new objects that escape via parameter arguments.
     *
     * The below sample2 method receives an input parameter of type A,
     * which escapes as an argument.
     * Assigning a new object to one of its fields
     * means the object potentially escapes the method,
     * hence it can't be optimized to stack allocation:
     *
     * define i32 @exact.org.example.ea.samples.EASample_03_ParameterEscape.sample2.s32.1.ref.1.class.org-example-ea-samples-EASample_03_ParameterEscape$A(i8 addrspace(1)* %thr0, i8 addrspace(1)* %p0) "frame-pointer"="non-leaf" uwtable gc "statepoint-example" !dbg !49 {
     *     ...
     *     %L4 = call i8 addrspace(1)* (i8 addrspace(1)*, i64, i32) @exact.org.qbicc.runtime.gc.nogc.NoGcHelpers.allocate.ref.1.class.java-lang-Object.2.s64.s32(i8 addrspace(1)* %thr0, i64 16, i32 8), !dbg !50 ; EASample_03_ParameterEscape.java:48 bci@1
     *     %L5 = bitcast i8 addrspace(1)* %L4 to %T.org.example.ea.samples.EASample_03_ParameterEscape$A addrspace(1)*, !dbg !50 ; EASample_03_ParameterEscape.java:48 bci@1
     *
     * The A instance in sample2 method could still be allocated as thread local,
     * meaning that any potential synchronization could be skipped.
     *
     * The new A instance created in sample does not escape,
     * and hence can be optimized to be stack allocated:
     *
     * define i32 @exact.org.example.ea.samples.EASample_03_ParameterEscape.sample.s32.0(i8 addrspace(1)* %thr0) "frame-pointer"="non-leaf" uwtable gc "statepoint-example" !dbg !33 {
     *     ...
     *     %L4 = alloca %T.org.example.ea.samples.EASample_03_ParameterEscape$A, i32 1, align 8
     */

    public static void main(String[] args)
    {
        putchar(sample() == 20 ? '.' : 'F');
    }

    static int sample()
    {
        A a = new A();
        a.aField = 10;

        int result = sample2(a);
        return result;
    }

    static int sample2(A a)
    {
        a.next = new A();
        a.next.aField = 20;
        return a.next.aField;
    }

    public static class A
    {
        int aField;
        A next;
    }

    @extern
    public static native int putchar(int arg);
}
