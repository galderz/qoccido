package org.example.ea.samples;

import org.example.ea.Main;

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
        Main.print(sample(10) == 10 ? '.' : 'F');

        // Main.print(sample(20) == 20 ? '.' : 'F'); -- TODO calling again results in A escaping, why?
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

    // Only relevant for JVM runs to print escape analysis:
    // You need to initialize the object once to get class initialized
    // C2 rightfully bails when EA encounters uninitialized class.
    // So, before the method call, add:
    // new A();
}
