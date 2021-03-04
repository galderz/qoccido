package org.mendrugo.qoccinero;

sealed interface Recipe
{
    final record BinaryOperator(String operator, Class<?> type1, Class<?> type2) implements Recipe
    {
        static org.mendrugo.qoccinero.Recipe.BinaryOperator of(String operator, Class<?> type)
        {
            return new org.mendrugo.qoccinero.Recipe.BinaryOperator(operator, type, type);
        }
    }

    final record StaticMethod(String methodName, Class<?> type, Recipe.StaticMethod before) implements Recipe
    {
        Recipe.StaticMethod compose(Recipe.StaticMethod before)
        {
            return new Recipe.StaticMethod(methodName, type, before);
        }

        static Recipe.StaticMethod of(String methodName, Class<?> type)
        {
            return new Recipe.StaticMethod(methodName, type, null);
        }
    }
}
