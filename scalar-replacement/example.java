import static org.qbicc.runtime.CNative.*;

public class example
{
    final int fieldA;
    final int fieldB;
    final int fieldC;

    public example(int a, int b, int c)
    {
        this.fieldA = a;
        this.fieldB = b;
        this.fieldC = c;
    }

    @extern
    public static native int putchar(int arg);

    static int single(int a, int b, int c)
    {
        example obj = new example(a, b, c);
        return obj.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
