package example;

import java.lang.System;

import static cc.quarkus.qcc.runtime.CNative.*;

public class Example
{
    @extern
    public static native int putchar(int arg);

    @export
    public static int main()
    {
        putchar('b');
        putchar('y');
        putchar('t');
        putchar('e');
        putchar('\n');

        {
            byte low = Byte.MIN_VALUE + 1;
            byte high = Byte.MAX_VALUE - 1;
            putchar(low - low == 0 ? '1' : '0');
            putchar(high - high == 0 ? '1' : '0');
            putchar(low - (byte)((short) low) == 0 ? '1' : '0');
            putchar(high - (byte)((short) high) == 0 ? '1' : '0');
            putchar(low - (byte)((int) low) == 0 ? '1' : '0');
            putchar(high - (byte)((int) high) == 0 ? '1' : '0');
            putchar(low - (byte)((long) low) == 0 ? '1' : '0');
            putchar(high - (byte)((long) high) == 0 ? '1' : '0');
            putchar(low - (byte)((float) low) == 0 ? '1' : '0');
            putchar(high - (byte)((float) high) == 0 ? '1' : '0');
            putchar(low - (byte)((double) low) == 0 ? '1' : '0');
            putchar(high - (byte)((double) high) == 0 ? '1' : '0');
        }

        putchar('\n');
        putchar('s');
        putchar('h');
        putchar('o');
        putchar('r');
        putchar('t');
        putchar('\n');

        {
            short low = Short.MIN_VALUE + 1;
            short high = Short.MAX_VALUE - 1;
            putchar(low - low == 0 ? '1' : '0');
            putchar(high - high == 0 ? '1' : '0');
            putchar(low - (short)((int) low) == 0 ? '1' : '0');
            putchar(high - (short)((int) high) == 0 ? '1' : '0');
            putchar(low - (short)((long) low) == 0 ? '1' : '0');
            putchar(high - (short)((long) high) == 0 ? '1' : '0');
            putchar(low - (short)((float) low) == 0 ? '1' : '0');
            putchar(high - (short)((float) high) == 0 ? '1' : '0');
            putchar(low - (short)((double) low) == 0 ? '1' : '0');
            putchar(high - (short)((double) high) == 0 ? '1' : '0');
        }

        putchar('\n');
        putchar('c');
        putchar('h');
        putchar('a');
        putchar('r');
        putchar('\n');

        {
            char low = Character.MIN_VALUE + 1;
            char high = Character.MAX_VALUE - 1;
            putchar(low - low == 0 ? '1' : '0');
            putchar(high - high == 0 ? '1' : '0');
            putchar(low - (char)((int) low) == 0 ? '1' : '0');
            putchar(high - (char)((int) high) == 0 ? '1' : '0');
            putchar(low - (char)((long) low) == 0 ? '1' : '0');
            putchar(high - (char)((long) high) == 0 ? '1' : '0');
            putchar(low - (char)((float) low) == 0 ? '1' : '0');
            putchar(high - (char)((float) high) == 0 ? '1' : '0');
            putchar(low - (char)((double) low) == 0 ? '1' : '0');
            putchar(high - (char)((double) high) == 0 ? '1' : '0');
        }

        putchar('\n');
        putchar('i');
        putchar('n');
        putchar('t');
        putchar('\n');

        {
            int low = Integer.MIN_VALUE + 1;
            int high = Integer.MAX_VALUE - 1;
            putchar(low - low == 0 ? '1' : '0');
            putchar(high - high == 0 ? '1' : '0');
            putchar(low - (int)((long) low) == 0 ? '1' : '0');
            putchar(high - (int)((long) high) == 0 ? '1' : '0');
            putchar(low - (int)((float) low) == 1 ? '1' : '0'); // loss of precision
            putchar(high - (int)((float) high) == -1 ? '1' : '0'); // loss of precision
            putchar(low - (int)((double) low) == 0 ? '1' : '0');
            putchar(high - (int)((double) high) == 0 ? '1' : '0');
        }

        putchar('\n');
        putchar('l');
        putchar('o');
        putchar('n');
        putchar('g');
        putchar('\n');
        {
            long low = Long.MIN_VALUE + 1;
            long high = Long.MAX_VALUE - 1;
            putchar(low - low == 0 ? '1' : '0');
            putchar(high - high == 0 ? '1' : '0');
            putchar(low - (long)((float) low) == 1 ? '1' : '0'); // loss of precision
            putchar(high - (long)((float) high) == -1 ? '1' : '0'); // loss of precision
            putchar(low - (long)((double) low) == 1 ? '1' : '0'); // loss of precision
            putchar(high - (long)((double) high) == -1 ? '1' : '0'); // loss of precision
        }

        putchar('\n');
        return 0;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

}
