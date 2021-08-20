package org.example.ea;

import org.example.ea.samples.EASample_01_Basic;
import org.example.ea.samples.EASample_02_StaticAssignment;
import org.example.ea.samples.EASample_03_ParameterEscape;
import org.example.ea.samples.EASample_04_ReturnEscape;

import static org.qbicc.runtime.CNative.*;

public class Main
{
    public static void main(String[] args)
    {
        EASample_01_Basic.main(args);
        EASample_02_StaticAssignment.main(args);
        EASample_03_ParameterEscape.main(args);
        EASample_04_ReturnEscape.main(args);
        print('\n');
    }

    public static void print(int arg)
    {
        try
        {
            putchar(arg);
        } catch (UnsatisfiedLinkError ignore)
        {
            System.out.print((char) arg);
        }
    }

    @extern
    public static native int putchar(int arg);
}
