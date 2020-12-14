import java.lang.System;

import static cc.quarkus.qcc.runtime.CNative.*;

public class hello
{
    @extern
    public static native int putchar(int arg);

    @export
    public static int main()
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
        return 0;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

}
