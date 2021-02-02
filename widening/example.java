import static cc.quarkus.qcc.runtime.CNative.*;

public class example
{
    @export
    public static int main()
    {
        Widening.main();
        Narrowing.main();
        UnaryPromotion.main();
        Negate.main();
        BinaryPromotion.main();
//        Errors.errors();
        return 0;
    }

    @extern
    public static native int putchar(int arg);

    public static void main(String[] args)
    {
        // make driver happy
    }
}
