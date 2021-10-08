package org.example.ea;

import org.example.ea.samples.EASample_01_Basic;
import org.example.ea.samples.EASample_02_StaticAssignment;
import org.example.ea.samples.EASample_03_ParameterEscape;
import org.example.ea.samples.EASample_04_ReturnEscape;
import org.example.ea.samples.EASample_05_Throw;
import org.example.ea.samples.EASample_06_ParameterToStatic;
import org.example.ea.samples.EASample_07_Runnable;

import static org.qbicc.runtime.CNative.*;

public class Main
{
    public static void main(String[] args)
    {
        EASample_01_Basic.main(args);
        EASample_02_StaticAssignment.main(args);
        EASample_03_ParameterEscape.main(args);
        EASample_04_ReturnEscape.main(args);
        EASample_05_Throw.main(args);
        EASample_06_ParameterToStatic.main(args);
        EASample_07_Runnable.main(args);
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
