package org.mendrugo.qoccinero;

import io.vavr.collection.List;

public record TypeRecipe(
    String name
    , List<StaticMethodRecipe> staticMethods
    , List<BinaryOperatorRecipe> binaryOperators
)
{
    static TypeRecipe of(String name)
    {
        return new TypeRecipe(name, List.empty(), List.empty());
    }

    TypeRecipe addStaticMethod(StaticMethodRecipe staticMethod)
    {
        return new TypeRecipe(name, staticMethods.append(staticMethod), binaryOperators);
    }

    TypeRecipe addBinaryOperator(BinaryOperatorRecipe binaryOperator)
    {
        return new TypeRecipe(name, staticMethods, binaryOperators.append(binaryOperator));
    }
}
