package org.mendrugo.qoccinero;

sealed interface Recipe
{
    final record BinaryOperator(Recipe left, String operator, Recipe right) implements Recipe
    {
        static BinaryOperator of(String operator, Class<?> type)
        {
            return new BinaryOperator(Type.of(type), operator, Type.of(type));
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

    final record Literal(Object obj) implements Recipe
    {
        static Literal of(Object obj)
        {
            return new Literal(obj);
        }
    }
}
