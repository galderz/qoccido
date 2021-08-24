package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_05_Throw
{
    public static void main(String[] args)
    {
        Main.print(sample(10) == 10 ? '.' : 'F');
    }

    static int sample(int data)
    {
        return sampleCatchAll(data);
    }

    static int sampleCatchAll(int data)
    {
        try
        {
            return sampleCatchNull();
        }
        catch (Exception e)
        {
            return data;
        }
    }

    public static int sampleCatchNull()
    {
        try
        {
            sampleThrow();
            return 30;
        }
        catch (NullPointerException e)
        {
            return 40;
        }
    }

    public static void sampleThrow()
    {
        throw new ArithmeticException();
    }
}
