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
     * E.g. stack allocate the object.
     */

    static int sample1(int data)
    {
        A a = new A();
        a.aField = data;
        return a.aField;
    }

    public static void main(String[] args)
    {
        Main.print(sample1(10) == 10 ? '.' : 'F');
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
