package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_08_CallTwice
{
    static int sample1(int data)
    {
        A a = new A();
        a.aField = data;
        return a.aField;
    }

    public static void main(String[] args)
    {
        Main.print(sample1(10) == 10 ? '.' : 'F');
        Main.print(sample1(20) == 20 ? '.' : 'F');
    }

    public static class A
    {
        int aField;
    }
}
