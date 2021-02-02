import static cc.quarkus.qcc.runtime.CNative.*;

public class Errors
{
    public static void errors()
    {
    }

    @extern
    public static native int putchar(int arg);
}
