package org.example.ea.samples;

import org.example.ea.Main;

import java.util.AbstractList;
import java.util.Iterator;

public class EASample_11_Loops
{
    public static void main(String[] args)
    {
        ObjectIdentity.main();
        ForLoop.main();
        RecursiveLoop.main();
    }

    static class RecursiveLoop
    {
        public static void main()
        {
            final Data.Int input = new Data.Int(9);
            Main.print(fibonacci(input).data == 34 ? '.' : 'F');
        }

        static Data.Int fibonacci(Data.Int n)
        {
            if (n.data <= 1)
                return n;

            final Data.Int nMinus1 = new Data.Int(n.data - 1);
            final Data.Int nMinus1Fib = fibonacci(nMinus1);

            final Data.Int nMinus2 = new Data.Int(n.data - 2);
            final Data.Int nMinus2Fib = fibonacci(nMinus2);
            return new Data.Int(nMinus1Fib.data + nMinus2Fib.data);
        }
    }

    static class ForLoop
    {
        public static void main()
        {
            Main.print(forEachList(10) == 3_628_800 ? '.' : 'F');
            Main.print(listOf(10).size == 10 ? '.' : 'F');
        }

        static int forEachList(int max)
        {
            Data.Int.List values = new Data.Int.List(max);
            for (int i = 0; i < max; i++)
            {
                values.add(new Data.Int(i + 1));
            }

            Data.Int result = new Data.Int(1);
            for (Data.Int value : values)
            {
                result = new Data.Int(result.data * value.data);
            }

            return result.data;
        }

        static Data.Int.List listOf(int max)
        {
            Data.Int.List values = new Data.Int.List(max);
            for (int i = 0; i < max; i++)
            {
                values.add(new Data.Int(i + 1));
            }
            return values;
        }
    }

    static class ObjectIdentity
    {
        public static void main()
        {
            Main.print(increment(1) == 2 ? '.' : 'F');
        }

        static int increment(int current)
        {
            Data.Int result = new Data.Int(current);
            Data.Int initial = result;
            while (initial == result)
            {
                result = new Data.Int(initial.data + 1);
            }
            return result.data;
        }
    }

    static class Data
    {
        static final class Int
        {
            final int data;

            Int(int data) {
                this.data = data;
            }

            static final class List extends AbstractList<Data.Int>
            {
                final Int[] data;
                int size;

                List(int size)
                {
                    this.data = new Int[size];
                }

                @Override
                public void add(int index, Int element)
                {
                    data[index] = element;
                    size = size + 1;
                }

                @Override
                public Int get(int index)
                {
                    return data[index];
                }

                @Override
                public int size()
                {
                    return size;
                }

                @Override
                public Iterator<Int> iterator() {
                    return new Itr();
                }

                final class Itr implements Iterator<Int>
                {
                    int cursor = 0;

                    @Override
                    public boolean hasNext()
                    {
                        return cursor != size();
                    }

                    @Override
                    public Int next()
                    {
                        Int next = get(cursor);
                        cursor = cursor + 1;
                        return next;
                    }
                }
            }
        }
    }
}
