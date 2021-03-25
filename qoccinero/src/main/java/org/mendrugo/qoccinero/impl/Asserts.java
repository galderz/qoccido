package org.mendrugo.qoccinero.impl;

final class Asserts
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
