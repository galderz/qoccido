package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_09_Interfaces
{
    /*
     * This sample showcases the effects of calls on interfaces,
     * and the impact implementations have on whether values escape or not.
     */

    public static void main(String[] args)
    {
        /*
         * In the monomorphic interface sample,
         * there are 2 implementations for the interface,
         * one of which causes the argument to the interface method to global escape.
         * As a result, calling that interface should pessimistically assume that the argument is always global escape.
         */
        Monomorphic.monoEscape();

        // TODO polymorphic

        // TODO no escape
    }

    static class Monomorphic
    {
        private static void monoEscape()
        {
            Main.print(!isInfinite(0, 0) ? '.' : 'F');
            Main.print(isInfinite(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
            Main.print(!isZero(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
            Main.print(isZero(0, 0) ? '.' : 'F');
        }

        static boolean isInfinite(int x, int y)
        {
            final Point point = new Point(x, y);
            final IsInfinite isInfinite = new IsInfinite();
            return testPoint(point, isInfinite);
        }

        static boolean isZero(int x, int y)
        {
            final Point point = new Point(x, y);
            final IsZero isZero = new IsZero();
            return testPoint(point, isZero);
        }

        static boolean testPoint(Point p, PointPredicate predicate)
        {
            return predicate.test1(p);
        }

        interface PointPredicate
        {
            Boolean test1(Point t1);
        }

        static final class IsInfinite implements PointPredicate
        {
            @Override
            public Boolean test1(Point p)
            {
                return p.x == Integer.MAX_VALUE && p.y == Integer.MAX_VALUE;
            }
        }

        static final class IsZero implements PointPredicate
        {
            static Point point;

            @Override
            public Boolean test1(Point p)
            {
                point = p;
                return point.x == 0 && point.y == 0;
            }
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
}
