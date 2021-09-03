package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_03_ParameterEscape
{
    /**
     * This sample showcases the effects of new objects that escape via parameter arguments.
     *
     * The below sample2 method receives an input parameter of type A,
     * which escapes as an argument.
     * Assigning a new object to one of its fields
     * means the object potentially escapes the method,
     * hence it can't be optimized to stack allocation.
     *
     * The A instance in sample2 method could still be allocated as thread local,
     * meaning that any potential synchronization could be skipped.
     *
     * The new A instance created in sample does not escape,
     * and hence can be optimized to be stack allocated.
     */

    public static void main(String[] args)
    {
        Main.print(sample() == 20 ? '.' : 'F');
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
}
