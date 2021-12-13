package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_02_StaticAssignment
{
    /*
     * This sample showcases the effects of static variable assignment.
     *
     * Objects assigned to static variables are considered to escape.
     * Such objects don't get any optimizations applied.
     * Hence they're allocated as usual in the heap.
     */

    static A sink;

    static int sample1(int data)
    {
        A a = new A();
        a.aField = data;

        sink = a;

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
}
