package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_11_Loops
{
    public static void main(String[] args)
    {
        ObjectIdentity.main();
    }

    static class ObjectIdentity
    {
        public static void main()
        {
            Main.print(increment(1) == 2 ? '.' : 'F');
        }

        static int increment(int current)
        {
            Counter result = new Counter(current);
            Counter initial = result;
            while (initial == result)
            {
                result = new Counter(initial.count + 1);
            }
            return result.count;
        }
    }

    static final class Counter
    {
        final int count;

        Counter(int count)
        {
            this.count = count;
        }
    }
}
