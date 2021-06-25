import static org.qbicc.runtime.CNative.*;

/**
 * An example for handling parameters that are assigned to `p.f`.
 */
public class example
{
    int data;
    example exampleRef;

    @extern
    public static native int putchar(int arg);

    static int first()
    {
        example e = new example();
        e.data = 10;
        return second(e);
    }

    static int second(example paramE) {
        paramE.exampleRef = new example();
        paramE.exampleRef.data = 20;
        return paramE.exampleRef.data;
    }

    public static void main(String[] args)
    {
        putchar(first() == 20 ? '.' : 'F');
    }
}
