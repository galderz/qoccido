package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_05_Throw
{
    /*
     * This samples showcases the effects of throwing new exceptions.
     * Such new instances that propagated methods are marked as argument escaping,
     * hence they should be allocated in the heap.
     */

    static int sample1(int data)
    {
        return sample2(data);
    }

    static int sample2(int data)
    {
        try
        {
            return sample3();
        }
        catch (Exception e)
        {
            return data;
        }
    }

    public static int sample3()
    {
        try
        {
            sample4();
            return 30;
        }
        catch (NullPointerException e)
        {
            return 40;
        }
    }

    public static void sample4()
    {
        throw new ArithmeticException();
    }

    public static void main(String[] args)
    {
        Main.print(sample1(10) == 10 ? '.' : 'F');
    }
}
