package org.mendrugo.qoccinero;

import io.vavr.Function1;

public record UnaryOperator<T>(
    String operator
    , Expression<T> expression
) implements Expression<T>
{
    static <T> UnaryOperator<T> of(Recipe.UnaryOperator recipe)
    {
        return new UnaryOperator<>(
            recipe.operator()
            , Expression.of(recipe.recipe())
        );
    }

    @Override
    public String id()
    {
        return String.format(
            "%s_%s"
            , operator.replace("-", "minus")
            , expression.id()
        );
    }

    @Override
    public ParamType<T> returns()
    {
        return expression.returns();
    }

    @Override
    public Expects<T> expects()
    {
        final var expects = expression.expects();
        return new Expects<T>(
            invoke(expects.value())
            , String.format("%s%s", operator, expects.createdBy())
        );
    }

    private T invoke(Object v1)
    {
        return Unchecked.cast(
            switch (operator)
            {
                case "-" -> negate(v1);
                default -> throw new IllegalStateException("Unexpected value: " + operator);
            }
        );
    }

    private Object negate(Object v1)
    {
        return invokeUnary(
            v1
            , (d1) -> -d1
            , (f1) -> -f1
            , (i1) -> -i1
            , (l1) -> -l1
        );
    }

    private Object invokeUnary(
        Object v1
        , Function1<Double, Double> doubleFn
        , Function1<Float, Float> floatFn
        , Function1<Integer, Integer> intFn
        , Function1<Long, Long> longFn
    )
    {
        if (v1 instanceof Double d1)
        {
            return doubleFn.apply(d1);
        }
        else if (v1 instanceof Float f1)
        {
            return floatFn.apply(f1);
        }
        else if (v1 instanceof Integer i1)
        {
            return intFn.apply(i1);
        }
        else if (v1 instanceof Long l1)
        {
            return longFn.apply(l1);
        }

        throw new RuntimeException(String.format(
            "invoke unary not handled for combination of v1 class %s"
            , v1.getClass()
        ));
    }
}
