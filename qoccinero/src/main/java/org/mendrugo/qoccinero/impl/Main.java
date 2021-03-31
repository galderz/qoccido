package org.mendrugo.qoccinero.impl;

public final class Main
{
    static void main()
    {
        // [x] 1. E -> T*N (expression -> hole types)
        // [x] 2a. N -> functionN() (number of holes -> function of same cardinal as number of holes)
        // [x] 2b. N -> scriptN() (number of holes -> script of same cardinal as number of holes)
        // [ ] 2c. T*N -> V*N (hole types -> values for types)
        // [ ] 3a. C -> V*N -> functionN() -> returns
        // [ ] 3b. C -> V*N -> scriptN() -> script
        // [ ] 4. (script, returns) -> assert script
        // [ ] 5. assert script -> MethodSpec
    }
}
