package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_09_Interface
{
    /*
     * This sample showcases the effects of calls on interfaces,
     * and the impact implementations have on whether values escape or not.
     *
     * In the global escape sample,
     * an interface has 2 implementations,
     * one of which causes the argument to the interface method to global escape.
     * As a result, calling that interface should pessimistically assume that the argument is always global escape.
     *
     * TODO no escape sample
     */

    public static void main(String[] args)
    {
        globalEscape();
    }

    static boolean isInfinite(int x, int y)
    {
        final Point point = new Point(x, y);
        final IsInfinite isInfinite = new IsInfinite();
        return test(point, isInfinite);
    }

    static boolean isZero(int x, int y)
    {
        final Point point = new Point(x, y);
        final IsZero isZero = new IsZero();
        return test(point, isZero);
    }

    static boolean test(Point p, Function1<Point, Boolean> predicate)
    {
        return predicate.apply(p);
    }

    private static void globalEscape()
    {
        Main.print(!isInfinite(0, 0) ? '.' : 'F');
        Main.print(isInfinite(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
        Main.print(!isZero(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
        Main.print(isZero(0, 0) ? '.' : 'F');
    }

    static final class IsInfinite implements Function1<Point, Boolean>
    {
        @Override
        public Boolean apply(Point p)
        {
            return p.x == Integer.MAX_VALUE && p.y == Integer.MAX_VALUE;
        }
    }

    static final class IsZero implements Function1<Point, Boolean>
    {
        static Point point;

        @Override
        public Boolean apply(Point p)
        {
            point = p;
            return point.x == 0 && point.y == 0;
        }
    }

    static final class Point
    {
        final int x;
        final int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @FunctionalInterface
    interface Function1<T1, R>
    {
        R apply(T1 t1);
    }
}
