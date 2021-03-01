package qoccinero;

public class Unchecked
{
    @SuppressWarnings("unchecked")
    static <T> T cast(Object o)
    {
        return (T) o;
    }
}
