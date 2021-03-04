package org.mendrugo.qoccinero;

import io.vavr.collection.List;

public record TypeRecipe(
    String name
    , List<Recipe.StaticMethod> staticMethods
    , List<Recipe.BinaryOperator> binaryOperators
)
{
    static TypeRecipe of(String name)
    {
        return new TypeRecipe(name, List.empty(), List.empty());
    }

    TypeRecipe addStaticMethod(Recipe.StaticMethod staticMethod)
    {
        return new TypeRecipe(name, staticMethods.append(staticMethod), binaryOperators);
    }

    TypeRecipe addBinaryOperator(Recipe.BinaryOperator binaryOperator)
    {
        return new TypeRecipe(name, staticMethods, binaryOperators.append(binaryOperator));
    }
}
