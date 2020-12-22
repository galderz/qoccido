import java.lang.System;

import static cc.quarkus.qcc.runtime.CNative.*;

public class main
{
    @extern
    public static native int putchar(int arg);

    @export
    public static int main()
    {
//        putchar(1 == 1 ? '1' : '0');
//        putchar(1 != 2 ? '1' : '0');

        int a = 1;
        int b = 1;
        if (a == b)
            putchar('1');
        else
            putchar('0');

        // putchar(a == b ? '1' : '0');
        return 0;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

}
