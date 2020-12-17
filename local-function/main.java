import static cc.quarkus.qcc.runtime.CNative.*;

public class main
{
    @extern
    public static native int putchar(char arg);

    @export
    public static int main()
    {
        print('h');
        print('\n');
        return 0;
    }

    static void print(char c)
    {
        putchar(c);
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

}
