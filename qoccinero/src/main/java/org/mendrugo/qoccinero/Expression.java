package org.mendrugo.qoccinero;

sealed interface Expression<T> permits Literal, StaticMethod, Constant
{
    String id();

    ParamType<T> returns();

    Expects<T> expects();

    static <T> Expression<T> of(Recipe recipe)
    {
        if (recipe instanceof Recipe.Type t)
        {
            return Literal.of(ParamType.of(t.type()));
        }

        if (recipe instanceof Recipe.Constant c)
        {
            return Constant.of(Unchecked.cast(c.constant()));
        }

        if (recipe instanceof Recipe.StaticMethod s)
        {
            return StaticMethod.of(s);
        }

        throw new RuntimeException("Unsupported recipe: " + recipe);
    }
}
