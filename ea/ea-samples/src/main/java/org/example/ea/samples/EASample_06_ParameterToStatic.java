package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_06_ParameterToStatic
{
    /*
     * This sample showcases the effects of assigning a parameter object into a static field.
     */

    public static void main(String[] args)
    {
        AssignWrite.main();
        AssignOnly.main();
        Casting.main();
    }

    static class AssignWrite
    {
        static A aw;

        private static void main()
        {
            Main.print(assignWrite() == 11 ? '.' : 'F');
        }

        static int assignWrite()
        {
            // New instance of A assigned to variable `a`.
            final A a = new A();
            a.aField = 10;
            // `a` variable passed as parameter.
            return doAssignWrite(a);
            // When the method returns, `a` can no longer be considered not escape,
            // because within the method it was assigned to a static variable,
            // and hence it's now considered to global escape.
        }

        static int doAssignWrite(A a)
        {
            a.next = new A();
            a.next.aField = 11;

            // Static assignment, so `a` goes from argument escape to global escape.
            // In turn, the A instance assigned to `a.next` goes from not escape to global escape.
            aw = a;

            return a.next.aField;
        }
    }

    static class AssignOnly
    {
        static A ao;

        private static void main()
        {
            Main.print(assignOnly() == 0 ? '.' : 'F');
        }

        static int assignOnly()
        {
            // New instance of A assigned to variable `a`.
            final A a = new A();
            // `a` variable passed as parameter.
            return doAssignOnly(a);
            // When the method returns, `a` can no longer be considered not escape,
            // because within the method it was assigned to a static variable,
            // and hence it's now considered to global escape.
        }

        static int doAssignOnly(A a)
        {
            // Static assignment, so `a` goes from argument escape to global escape.
            ao = a;
            return a.aField;
        }
    }

    static class Casting
    {
        static A ac;

        public static void main()
        {
            Main.print(casting() == 20 ? '.' : 'F');
        }

        static int casting()
        {
            final A a = new A();
            a.aField = 20;
            return generic(a);
        }

        static int generic(Object obj)
        {
            A a = (A) obj;
            return concrete(a);
        }

        static int concrete(A a)
        {
            ac = a;
            return a.aField;
        }
    }

    public static class A
    {
        int aField;
        A next;
    }
}
