import static org.qbicc.runtime.CNative.*;

/**
 * An example to see the effects of local variable assignment.
 * A deferred node should be set between the local variable,
 * and the variable to which the new object is assigned.
 */
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
        example localVariable = new example(a, b, c);
        example tmp = localVariable;
        return tmp.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
