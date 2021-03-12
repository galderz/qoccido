package org.mendrugo.qoccinero;

public class Unchecked
{
    @SuppressWarnings("unchecked")
    public static <T> T cast(Object o)
    {
        return (T) o;
    }
}
