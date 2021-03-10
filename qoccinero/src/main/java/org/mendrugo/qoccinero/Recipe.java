package org.mendrugo.qoccinero;

sealed interface Recipe
{
    final record BinaryOperator(Recipe left, String operator, Recipe right) implements Recipe
    {
        static BinaryOperator of(Recipe left, String operator, Recipe right)
        {
            return new BinaryOperator(left, operator, right);
        }

        static BinaryOperator of(String operator, Class<?> type)
        {
            return new BinaryOperator(Type.of(type), operator, Type.of(type));
        }
    }

    final record UnaryOperator(String operator, Recipe recipe) implements Recipe
    {
        static UnaryOperator of(String operator, Recipe recipe)
        {
            return new UnaryOperator(operator, recipe);
        }
    }

    final record StaticMethod(String methodName, Class<?> type, StaticMethod before) implements Recipe
    {
        StaticMethod compose(StaticMethod before)
        {
            return new StaticMethod(methodName, type, before);
        }

        static StaticMethod of(String methodName, Class<?> type)
        {
            return new StaticMethod(methodName, type, null);
        }
    }

    final record Type(Class<?> type) implements Recipe
    {
        static Type of(Class<?> type)
        {
            return new Type(type);
        }
    }

    final record Constant(Object constant) implements Recipe
    {
        static Constant of(Object constant)
        {
            return new Constant(constant);
        }
    }
}
