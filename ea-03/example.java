import static org.qbicc.runtime.CNative.*;

/**
 * An example to assign a new object to a static field.
 * The object should be set to global escape,
 * and it should be allocated in the heap.
 */
public class example
{
    static example staticVariable;

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
        staticVariable = new example(a, b, c);
        return staticVariable.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
