package org.example.ea;

import org.example.ea.samples.EASample_01_Basic;
import org.example.ea.samples.EASample_02_StaticAssignment;
import org.example.ea.samples.EASample_03_ParameterEscape;
import org.example.ea.samples.EASample_04_ReturnEscape;
import org.qbicc.runtime.CNative;

import static org.qbicc.runtime.CNative.*;

public class Main
{
    public static void main(String[] args)
    {
        EASample_01_Basic.main(args);
        EASample_02_StaticAssignment.main(args);
        EASample_03_ParameterEscape.main(args);
        EASample_04_ReturnEscape.main(args);
        putchar('\n');
    }

    @extern
    public static native int putchar(int arg);
}
