package org.mendrugo.qoccinero;

public record StaticMethodRecipe(String methodName, Class<?> type, StaticMethodRecipe before)
{
    StaticMethodRecipe compose(StaticMethodRecipe before)
    {
        return new StaticMethodRecipe(methodName, type, before);
    }

    static StaticMethodRecipe of(String methodName, Class<?> type)
    {
        return new StaticMethodRecipe(methodName, type, null);
    }
}
