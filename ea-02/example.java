import static org.qbicc.runtime.CNative.*;

/**
 * An example to see the effects of local variable assignment.
 * A deferred node should be set between the local variable,
 * and the variable to which the new object is assigned.
 */
public class example
{
    int fieldA;
    int fieldB;
    int fieldC;

    @extern
    public static native int putchar(int arg);

    static int single(int a, int b, int c)
    {
        example localVariable = new example();
        localVariable.fieldA = a;
        localVariable.fieldB = b;
        localVariable.fieldC = c;
        example tmp = localVariable;
        return tmp.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
