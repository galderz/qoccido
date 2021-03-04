package org.mendrugo.qoccinero;

sealed interface Recipe
{
    final record BinaryOperator(String operator, Recipe left, Recipe right) implements Recipe
    {
        static org.mendrugo.qoccinero.Recipe.BinaryOperator of(String operator, Class<?> type)
        {
            return new org.mendrugo.qoccinero.Recipe.BinaryOperator(operator, Type.of(type), Type.of(type));
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

    final record Type(Class<?> type) implements Recipe
    {
        static Recipe.Type of(Class<?> type)
        {
            return new Type(type);
        }
    }
}
