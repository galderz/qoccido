import static org.qbicc.runtime.CNative.*;

/**
 * An example for assignments of the following type:
 *
 * <code>
 *     p = new T();
 *     p.f = q; // <--
 * </code>
 */
public class example
{
    int data;
    example exampleRef;

    static int single()
    {
        example e = new example();
        e.data = 10;

        e.exampleRef = new example();
        e.exampleRef.data = 20;

        return e.exampleRef.data;
    }

    public static void main(String[] args)
    {
        putchar(single() == 20 ? '.' : 'F');
    }

    @extern
    public static native int putchar(int arg);
}
