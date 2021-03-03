package org.mendrugo.qoccinero;

public record BinaryOperatorRecipe(String operator, Class<?> type1, Class<?> type2)
{
    static BinaryOperatorRecipe of(String operator, Class<?> type)
    {
        return new BinaryOperatorRecipe(operator, type, type);
    }
}
