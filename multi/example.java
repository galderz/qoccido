import static cc.quarkus.qcc.runtime.CNative.*;

class example
{
    @export
    public static int main()
    {
        Foo.foochar('h');
        return 0;
    }

    public static void main(String[] args)
    {
        // make driver happy
    }
}