package org.example.ea.samples;

import static org.qbicc.runtime.CNative.*;

public class EASample_01_Basic
{
    public static void main(String[] args)
    {
        putchar(sample(10, 20, 30) == 10 ? '.' : 'F');
    }

    static int sample(int one, int two, int three)
    {
        A a = new A();
        a.one = one;
        a.two = two;
        a.three = three;
        return a.one;
    }

    public static class A
    {
        int one;
        int two;
        int three;
    }

    @extern
    public static native int putchar(int arg);
}
