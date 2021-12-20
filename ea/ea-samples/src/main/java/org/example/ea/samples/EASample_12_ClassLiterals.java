package org.example.ea.samples;

import org.example.ea.Asserts;

public class EASample_12_ClassLiterals
{
    public static void main(String[] args)
    {
        concrete();
        array();
        multi();
    }

    static void concrete()
    {
        Class<?> cls = dummy(MyClass.class);
        Asserts.equals(MyClass.class, cls);
        Asserts.equals("org.example.ea.samples.EASample_12_ClassLiterals$MyClass", cls.getName());
    }

    static void array()
    {
        Class<?> cls = dummy(MyClass[].class);
        Asserts.equals(MyClass[].class, cls);
        Asserts.equals("[Lorg.example.ea.samples.EASample_12_ClassLiterals$MyClass;", cls.getName());
    }

    static void multi()
    {
        Class<?> cls = dummy(MyClass[][].class);
        Asserts.equals(MyClass[][].class, cls);
        Asserts.equals("[[Lorg.example.ea.samples.EASample_12_ClassLiterals$MyClass;", cls.getName());
    }

    static Class<?> dummy(Class<?> cls)
    {
        return cls;
    }

    static class MyClass {}
}
