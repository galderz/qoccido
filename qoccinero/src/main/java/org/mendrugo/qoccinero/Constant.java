package org.mendrugo.qoccinero;

record Constant<T>(ParamType<T> returns, T constant) implements Expression<T>
{
    static <T> Constant<T> of(T obj)
    {
        return new Constant<>(ParamType.of(obj.getClass()), obj);
    }

    @Override
    public String id()
    {
        return constant.toString();
    }

    @Override
    public Expects<T> expects()
    {
        final var createdBy = String.format(
            "%s /* %s */"
            , returns.toLiteral().apply(constant)
            , returns.toHex(constant)
        );
        return new Expects<>(constant, createdBy);
    }
}
