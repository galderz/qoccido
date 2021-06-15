import static org.qbicc.runtime.CNative.*;

public class example
{
    int data;
    example u, l;
    static example x, y;

    static void L()
    {
        x = new example();
        y = x;
        T(x, y);
        putchar(x.data == 30 ? '.' : 'F');
        putchar(y.data == 30 ? '.' : 'F');
    }

    static void T(example f1, example f2)
    {
        f1.u = new example();
        f2.l = new example();
        f1.data = 10;
        f2.data = f2.data + 20;
        f2 = new example();
    }

    public static void main(String[] args)
    {
        L();
    }

    @extern
    public static native int putchar(int arg);
}
