package qoccinero;

public record StaticMethodRecipe(String methodName, String className, StaticMethodRecipe before)
{
    StaticMethodRecipe compose(StaticMethodRecipe before)
    {
        return new StaticMethodRecipe(methodName, className, before);
    }

    static StaticMethodRecipe of(String methodName, String className)
    {
        return new StaticMethodRecipe(methodName, className, null);
    }
}
