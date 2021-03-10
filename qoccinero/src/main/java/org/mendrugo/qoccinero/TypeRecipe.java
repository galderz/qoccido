package org.mendrugo.qoccinero;

import io.vavr.collection.List;

// TODO why not Type.Recipe?
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
        final var computedBinaryOperators = computesAsBinaryOperator(staticMethod);
        if (computedBinaryOperators.isEmpty())
        {
            return new TypeRecipe(name, staticMethods.append(staticMethod), binaryOperators);
        }
        else
        {
            return new TypeRecipe(name, staticMethods, binaryOperators.appendAll(computedBinaryOperators));
        }
    }

    TypeRecipe addBinaryOperator(Recipe.BinaryOperator binaryOperator)
    {
        return new TypeRecipe(name, staticMethods, binaryOperators.append(binaryOperator));
    }

    private static List<Recipe.BinaryOperator> computesAsBinaryOperator(Recipe.StaticMethod staticMethod)
    {
        if ("compare".equals(staticMethod.methodName()))
        {
            // Do not compare exact values returned by *.compare() functions.
            // Instead just verify that the returns are > 0 (>= 1), == 0, or < 0 (<= -1)
            return List.of(
                Recipe.BinaryOperator.of(staticMethod, ">=", Recipe.Constant.of(1))
                , Recipe.BinaryOperator.of(Recipe.Constant.of(1), "<=", staticMethod) // inverse ^
                , Recipe.BinaryOperator.of(staticMethod, "<", Recipe.Constant.of(1))
                , Recipe.BinaryOperator.of(Recipe.Constant.of(1), ">", staticMethod) // inverse ^

                , Recipe.BinaryOperator.of(staticMethod, "<=", Recipe.Constant.of(-1))
                , Recipe.BinaryOperator.of(Recipe.Constant.of(-1), ">=", staticMethod) // inverse ^
                , Recipe.BinaryOperator.of(staticMethod, ">", Recipe.Constant.of(-1))
                , Recipe.BinaryOperator.of(Recipe.Constant.of(-1), "<", staticMethod) // inverse ^

                , Recipe.BinaryOperator.of(staticMethod, ">", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(staticMethod, ">=", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(staticMethod, "<", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(staticMethod, "<=", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(staticMethod, "==", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(staticMethod, "!=", Recipe.Constant.of(0))
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), ">", staticMethod)
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), ">=", staticMethod)
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), "<", staticMethod)
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), "<=", staticMethod)
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), "==", staticMethod)
                , Recipe.BinaryOperator.of(Recipe.Constant.of(0), "!=", staticMethod)

                // , Recipe.BinaryOperator.of(Recipe.Constant.of(0), ">", Recipe.UnaryOperator.of("-", staticMethod))
            );
        }

        return List.of();
    }
}
