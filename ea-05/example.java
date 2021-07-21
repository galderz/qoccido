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

    static int single()
    {
        example e = new example();
        e.data = 10;

        final int result = single2(e);
        putchar(result);
        return result;
    }

    static int single2(example paramE) {
        paramE.exampleRef = new example();
        paramE.exampleRef.data = 20;
        return paramE.exampleRef.data;
    }

    public static void main(String[] args)
    {
        putchar(single() == 20 ? '.' : 'F');
    }
}
