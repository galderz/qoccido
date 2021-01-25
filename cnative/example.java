import static cc.quarkus.qcc.runtime.CNative.*;

public class example
{
    @extern
    public static native int putchar(c_int arg);

    @export
    public static c_int main(c_int argc, ptr<?> argv)
    {
        _Float32 foo = word(1.0f);
        _Float32 bar = word(1.0f);
        putchar(foo == bar ? word('.') : word('F'));

        return word(0);
    }

    public static void main(String[] args)
    {
        // make driver happy
    }
}
