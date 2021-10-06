package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_06_ParameterToStatic
{
    /**
     * This sample showcases the effects of assigning a parameter object into a static field.
     */

    static A sink;

    public static void main(String[] args)
    {
        Main.print(sample() == 20 ? '.' : 'F');
    }

    static int sample()
    {
        // New instance of A assigned to variable `a`.
        A a = new A();
        a.aField = 10;

        // `a` variable passed as parameter to sample2.
        sample2(a);
        // When sample2() returns, `a` can no longer be considered not escape,
        // because within sample2() it was assigned to a static variable,
        // and hence it's now considered to global escape.
        return sink.next.aField;
    }

    static void sample2(A a)
    {
        // New instance of A assigned to `a.next`.
        a.next = new A();
        a.next.aField = 20;

        // Static assignment, so `a` goes from argument escape to global escape.
        // In turn, the A instance assigned to `a.next` goes from not escape to global escape.
        sink = a;
    }

    public static class A
    {
        int aField;
        A next;
    }
}
