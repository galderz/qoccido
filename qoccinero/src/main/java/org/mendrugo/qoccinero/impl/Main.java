package org.mendrugo.qoccinero.impl;

import io.vavr.Function1;
import io.vavr.collection.List;

public final class Main
{
    static void main(int count, Expression expr)
    {
        // [x] 1. E -> T*N (expression -> hole types)
        List<Class<?>> types = Holes.types(expr);

        // [x] 2a. N -> functionN() (number of holes -> function of same cardinal as number of holes)
        // [x] 2b. N -> scriptN() (number of holes -> script of same cardinal as number of holes)
        // [x] 2c. T*N -> V*N (hole types -> values for types)
        // [ ] 3a. C -> V*N -> functionN() -> returns
        // [ ] 3b. C -> V*N -> scriptN() -> script
        // [ ] 4. (script, returns) -> assert script
        // [ ] 5. assert script -> MethodSpec
    }

    private static void main1(int count, Class<?> type, Expression expr)
    {
        // [x] 2a. N -> functionN() (number of holes -> function of same cardinal as number of holes)
        Function1<Object, Object> fn = Functions.function1(expr);
        
        // [x] 2b. N -> scriptN() (number of holes -> script of same cardinal as number of holes)
        Function1<String, String> scriptFn = Scripts.script1(expr);

        // [x] 2c. T*N -> V*N (hole types -> values for types)
        List<?> values = Values.values(count, type);

        // [ ] 3a. C -> V*N -> functionN() -> returns
        // [ ] 3b. C -> V*N -> scriptN() -> script
        // [ ] 4. (script, returns) -> assert script
        // [ ] 5. assert script -> MethodSpec
    }
}
