import static cc.quarkus.qcc.runtime.CNative.*;

public class example
{
    @export
    public static int main()
    {
        Widening.main();
        Narrowing.main();
        UnaryPromotion.main();
        return 0;
    }

    @extern
    public static native int putchar(int arg);

    public static void main(String[] args)
    {
        // make driver happy
    }
}
