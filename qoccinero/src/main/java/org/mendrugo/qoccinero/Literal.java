package org.mendrugo.qoccinero;

import io.vavr.Function0;
import io.vavr.collection.Iterator;

record Literal<T>(ParamType<T> returns, Function0<T> supplier) implements Expression<T>
{
    static <T> Literal<T> ofAll(ParamType<T> returns)
    {
        return new Literal<>(
            returns
            , new IteratorSupplier<>(Values.values(returns.arbitrary()).iterator())
        );
    }

    static <T> Literal<T> of(T obj)
    {
        return new Literal<>(
            ParamType.of(obj.getClass())
            , () -> obj
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
