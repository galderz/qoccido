import static org.qbicc.runtime.CNative.*;

/**
 * An example to achieve stack allocation of 'example' object.
 *
 * From:
 *   %L0 = call i8 addrspace(1)* (i8 addrspace(1)*, i64, i32) @exact.org.qbicc.runtime.gc.nogc.NoGcHelpers.allocate.ref.1.class.java-lang-Object.2.s64.s32(i8 a
 * ddrspace(1)* %thr0, i64 16, i32 8), !dbg !34
 *
 * To:
 *   %L0 = alloca %T.example, i32 1, align 8
 *
 * Note:
 * With further analysis, the object access can be strength reduced,
 * and the creation of object may be eliminated.
 */
public class example
{
    int fieldA;
    int fieldB;
    int fieldC;

    @extern
    public static native int putchar(int arg);

    static int single(int a, int b, int c)
    {
        example localVariable = new example();
        localVariable.fieldA = a;
        localVariable.fieldB = b;
        localVariable.fieldC = c;
        return localVariable.fieldA;
    }

    public static void main(String[] args)
    {
        putchar(single(10, 20, 30) == 10 ? '.' : 'F');
    }
}
