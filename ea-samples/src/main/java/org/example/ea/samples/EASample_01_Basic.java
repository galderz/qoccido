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
     */

    public static void main(String[] args)
    {
        putchar(sample(10) == 10 ? '.' : 'F');
    }

    static int sample(int data)
    {
        A a = new A();
        a.aField = data;
        return a.aField;
    }

    public static class A
    {
        int aField;
    }

    @extern
    public static native int putchar(int arg);
}
