import static cc.quarkus.qcc.runtime.CNative.*;

public class main
{
    @extern
    public static native int putchar(int arg);

    @export
    public static int main()
    {
        print('h');
        print('\n');
        return 0;
    }

    // add int return to avoid:
    // <stdin>:3: error: /opt/llvm/bin/llc: instructions returning void cannot have a name
    // change parameter type to int to avoid:
    // <stdin>:17: error: /opt/llvm/bin/llc: '%p0' defined with type 'i16' but expected 'i32'
    static int print(int c)
    {
        putchar(c);
        return 0;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }

}
