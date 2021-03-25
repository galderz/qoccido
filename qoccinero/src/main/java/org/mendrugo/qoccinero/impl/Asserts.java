package org.mendrugo.qoccinero.impl;

public class Asserts
{
    static String assertThat(String script, Object returns)
    {
        return String.format(
            "putchar(%s == (%s) ? '.' : 'F')"
            , returns
            , script
        );
    }
}
