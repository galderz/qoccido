package org.mendrugo.qoccinero;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.collection.Iterator;

record Literal<T>(ParamType<T> returns, Iterator<T> values) implements Expression<T>
{
    static <T> Literal<T> of(ParamType<T> returns)
    {
        return new Literal<T>(returns, Values.values(returns.arbitrary()).iterator());
    }

    @Override
    public String id()
    {
        return returns.type().getName().replace('.', '_');
    }

    @Override
    public Expects<T> expects()
    {
        final var value = values.next();
        final var createdBy = String.format(
            "%s /* %s */"
            , returns.toLiteral().apply(value)
            , returns.toHex(value)
        );
        return new Expects<>(value, createdBy);
    }
}
