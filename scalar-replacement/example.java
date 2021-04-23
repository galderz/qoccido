import static org.qbicc.runtime.CNative.*;

public class example
{
    final int exampleField;

    public example(int exampleParameter)
    {
        this.exampleField = exampleParameter;
    }

    @extern
    public static native int putchar(int arg);

    static int single(int x)
    {
        example obj = new example(x);
        return obj.exampleField;
    }

    public static void main(String[] args)
    {
        putchar(single(10) == 10 ? '.' : 'F');
    }
}
