package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_04_ReturnEscape
{
    /*
     * This sample showcases the effects of new objects that escape via objects returned by methods.
     * 
     * The below sample2 method instantiates an A instance,
     * but this is returned to the caller,
     * so it can't be stack allocated.
     */

    static int sample1()
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

    public static void main(String[] args)
    {
        Main.print(sample1() == 10 ? '.' : 'F');
    }

    public static class A
    {
        int aField;
    }
}
