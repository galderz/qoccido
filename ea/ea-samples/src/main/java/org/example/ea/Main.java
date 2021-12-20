package org.example.ea;

import org.example.ea.samples.EASample_01_Basic;
import org.example.ea.samples.EASample_02_StaticAssignment;
import org.example.ea.samples.EASample_03_ParameterEscape;
import org.example.ea.samples.EASample_04_ReturnEscape;
import org.example.ea.samples.EASample_05_Throw;
import org.example.ea.samples.EASample_06_ParameterToStatic;
import org.example.ea.samples.EASample_07_Runnable;
import org.example.ea.samples.EASample_08_CallTwice;
import org.example.ea.samples.EASample_09_Interfaces;
import org.example.ea.samples.EASample_10_Subclasses;
import org.example.ea.samples.EASample_11_Loops;
import org.example.ea.samples.EASample_12_ClassLiterals;

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
        EASample_08_CallTwice.main(args);
        EASample_09_Interfaces.main(args);
        EASample_10_gSubclasses.main(args);
        EASample_11_Loops.main(args);
        EASample_12_ClassLiterals.main(args);
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
