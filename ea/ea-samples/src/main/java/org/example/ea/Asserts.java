package org.example.ea;

public final class Asserts
{
    public static void equals(Object expected, Object actual)
    {
        if (expected.equals(actual))
        {
            System.out.print('.');
        }
        else
        {
            System.out.println('F');
            System.out.println("Assert failure. Expected " + expected + ", but got " + actual);
        }
    }
    
}
