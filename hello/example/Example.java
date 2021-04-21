package example;

import static org.qbicc.runtime.CNative.*;

public class Example
{
    @extern
    public static native int putchar(int arg);

    public static void main(String[] args)
    {
        putchar('h');
        putchar('e');
        putchar('l');
        putchar('l');
        putchar('o');
        putchar(' ');
        putchar('w');
        putchar('o');
        putchar('r');
        putchar('l');
        putchar('d');
        putchar('\n');
    }

}
