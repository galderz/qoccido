package org.mendrugo.qoccinero;

import io.vavr.Function0;
import io.vavr.collection.Iterator;

record Literal<T>(ParamType<T> returns, Function0<T> supplier) implements Expression<T>
{
    static <T> Literal<T> of(ParamType<T> returns)
    {
        return new Literal<>(
            returns
            , new IteratorSupplier<>(Values.values(returns.arbitrary()).iterator())
        );
    }

    @Override
    public String id()
    {
        return returns.type().getName().replace('.', '_');
    }

    @Override
    public Expects<T> expects()
    {
        final var value = supplier.apply();
        final var createdBy = String.format(
            "%s /* %s */"
            , returns.toLiteral().apply(value)
            , returns.toHex(value)
        );
        return new Expects<>(value, createdBy);
    }

    private static final record IteratorSupplier<T>(Iterator<T> iterator) implements Function0<T>
    {
        @Override
        public T apply()
        {
            return iterator.next();
        }
    }
}
