package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_06_ParameterToStatic
{
    /*
     * This sample showcases the effects of assigning a parameter object into a static field.
     */

    static A misc;
    static A only;

    static int setAndWrite_0()
    {
        // New instance of A assigned to variable `a`.
        final A a = new A();
        a.aField = 10;
        // `a` variable passed as parameter.
        return setAndWrite_1(a);
        // When the method returns, `a` can no longer be considered not escape,
        // because within the method it was assigned to a static variable,
        // and hence it's now considered to global escape.
    }

    static int setAndWrite_1(A a)
    {
        a.next = new A();
        a.next.aField = 11;

        // Static assignment, so `a` goes from argument escape to global escape.
        // In turn, the A instance assigned to `a.next` goes from not escape to global escape.
        misc = a;

        return a.next.aField;
    }

    static int setOnly_0()
    {
        // New instance of A assigned to variable `a`.
        final A a = new A();
        // `a` variable passed as parameter.
        return setOnly_1(a);
        // When the method returns, `a` can no longer be considered not escape,
        // because within the method it was assigned to a static variable,
        // and hence it's now considered to global escape.
    }

    static int setOnly_1(A a)
    {
        // Static assignment, so `a` goes from argument escape to global escape.
        only = a;
        return a.aField;
    }

    public static void main(String[] args)
    {
        Main.print(setAndWrite_0() == 11 ? '.' : 'F');
        Main.print(setOnly_0() == 0 ? '.' : 'F');
    }

    public static class A
    {
        int aField;
        A next;
    }
}
