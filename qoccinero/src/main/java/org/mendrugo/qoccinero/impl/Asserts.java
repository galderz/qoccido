package org.mendrugo.qoccinero.impl;

public class Asserts
{
    // [ ] Expression (E) -> number of holes (N)
    // [ ] N -> functionN() actual
    // [ ] N -> scriptN() expect
    // [ ] Expression (E) -> types of holes (T)
    // [ ] T -> valuesN()
    // [ ] valuesN() -> v -> functionN() / v -> scriptN() -> assert
    // assert -> MethodSpec

    static String assertThat(String script, Object returns)
    {
        return String.format(
            "putchar(%s == (%s) ? '.' : 'F')"
            , returns
            , script
        );
    }
}
