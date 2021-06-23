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

//    example a;
//    example b;
//    static example staticA;
//    static example staticB;

    @extern
    public static native int putchar(int arg);

//    static int first()
//    {
//        staticA = new example();
//        staticB = staticA;
//        return second(staticA, staticB);
//    }

//    static int second(example f1, example f2)
//    {
//        f1.a = new example();
//        f2.b = new example();
//        f1.data = 10;
//        f2.data = f2.data + 20;
//        f2 = new example();
//        return f1.data;
//    }

//    static int first()
//    {
//        return second(new example());
//    }
//
//    private static int second(example e1)
//    {
//        return e1.data;
//    }

    static int first()
    {
        example e = new example();
        e.data = 10;
        e.exampleRef = new example();
        e.exampleRef.data = 20;
        return e.exampleRef.data;
    }

    public static void main(String[] args)
    {
        putchar(first() == 20 ? '.' : 'F');
    }
}
