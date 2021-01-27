import static cc.quarkus.qcc.runtime.CNative.*;

public class Foo
{
    public static void foochar(int arg)
    {
        putchar(arg);
    }

    @extern
    public static native int putchar(int arg);
}