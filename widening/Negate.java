import static cc.quarkus.qcc.runtime.CNative.*;

public class Negate
{
    public static void main()
    {
        negateInt();
        negateFloat();
    }

    private static void negateInt()
    {
        putchar('-');
        putchar('.');
        putchar('i');
        putchar('n');
        putchar('t');
        putchar('\t');
        putchar('\t');

        byte b = 10;
        int ib = -b;
        putchar(-b == ib ? '.' : 'F');

        int i = 10;
        putchar(i - i == -i + i ? '.' : 'F');

        putchar('\n');
    }

    private static void negateFloat()
    {
        putchar('-');
        putchar('.');
        putchar('f');
        putchar('l');
        putchar('o');
        putchar('a');
        putchar('t');
        putchar('\t');
        putchar('\t');

        float f = 10.0f;
        float ff = -f;
        putchar(-f == ff ? '.' : 'F');

        putchar('\n');
    }

    @extern
    public static native int putchar(int arg);
}
