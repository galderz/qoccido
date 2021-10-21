package org.example.ea.samples;

import org.example.ea.Main;

import java.util.function.Predicate;

public class EASample_09_VirtualEscape
{
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

    static boolean test(Point p, Predicate<Point> predicate)
    {
        return predicate.test(p);
    }

    public static void main(String[] args)
    {
        Main.print(!isInfinite(0, 0) ? '.' : 'F');
        Main.print(isInfinite(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
        Main.print(!isZero(Integer.MAX_VALUE, Integer.MAX_VALUE) ? '.' : 'F');
        Main.print(isZero(0, 0) ? '.' : 'F');
    }

    static final class IsInfinite implements Predicate<Point>
    {
        @Override
        public boolean test(Point p)
        {
            return p.x == Integer.MAX_VALUE && p.y == Integer.MAX_VALUE;
        }
    }

    static final class IsZero implements Predicate<Point>
    {
        static Point point;

        @Override
        public boolean test(Point p)
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
}
