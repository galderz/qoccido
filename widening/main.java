import java.lang.System;

import static cc.quarkus.qcc.runtime.CNative.*;

public class main
{
    @extern
    public static native int putchar(int arg);

    @export
    public static int main()
    {
        print(12345);
        return 0;
    }

    static void print(final int num)
    {
        int current;
        if (num < 0)
        {
            putchar('-');
            current = -num;
        }
        else
        {
            current = num;
        }

        if (current == 0)
            return;

        int rest = current / 10;
        print(rest);

        int digit = current % 10;
        putchar((char) ('0' + digit));
    }

    public static void main(String[] args)
    {
        // make driver happy
    }
}
