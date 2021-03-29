import static cc.quarkus.qcc.runtime.CNative.*;

public class example
{
    final int x;

    public example(int x)
    {
        this.x = x;
    }

    @export
    public static int main()
    {
        putchar(single(10) == 10 ? '.' : 'F');
        return 0;
    }

    static int single(int x)
    {
        example obj = new example(x);
        return obj.x;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

    @extern
    public static native int putchar(int arg);

}
