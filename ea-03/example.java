import static org.qbicc.runtime.CNative.*;

/**
 * An example to assign a new object to a static field.
 * The object should be set to global escape,
 * and it should be allocated in the heap.
 */
public class example
{
    static example staticVariable;

    int fieldA;
    int fieldB;
    int fieldC;

    @extern
    public static native int putchar(int arg);

    static int single(int a, int b, int c)
    {
        staticVariable = new example();
        staticVariable.fieldA = a;
        staticVariable.fieldB = b;
        staticVariable.fieldC = c;
        return staticVariable.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
