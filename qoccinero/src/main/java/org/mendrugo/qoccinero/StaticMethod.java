package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.CheckedFunction2;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.Iterator;
import io.vavr.collection.List;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

record StaticMethod<R>(
    Method method
    , Class<?> clazz
    , List<Expression<?>> inputs
    , List<Iterator<?>> inputValues
    , ParamType<R> returns
    , StaticMethod<?> before
) implements Expression<R>
{
    MethodSpec toMethodSpec()
    {
        final var methodName = id();

        return switch (inputs.length())
        {
            case 1 -> MethodSpecs.toMethodSpec1(param0(), expected1(), returns, actual1(), methodName);
            case 2 -> MethodSpecs.toMethodSpec2(param0(), inputs.get(1), expected2(), actual2(), methodName);
            default -> throw new IllegalStateException("Unexpected value: " + inputs.length());
        };
    }

    @Override
    public String id()
    {
        if (Objects.isNull(before))
        {
            return methodId();
        }
        
        return String.format(
            "%s_%s"
            , methodId()
            , before.methodId()
        );
    }

    private String methodId()
    {
        return classMethod().replace('.', '_');
    }

    private Expression<?> param0()
    {
        return Objects.isNull(before)
            ? inputs.get(0)
            : before.param0();
    }

    private <T> Function1<T, R> invoke1()
    {
        // TODO why doesn't CheckedFunction2 combined with unchecked work?
        return v -> invoke(method, clazz, v);
    }

    private <T> Function1<T, R> expected1()
    {
//        Function1<T, R> invoke = Objects.isNull(before)
//            ? invoke1()
//            : invoke1().compose(before.invoke1());

        return Objects.isNull(before)
            ? invoke1()
            : invoke1().compose(before.invoke1());

//        return invoke
//            .andThen(ret -> returns.toLiteral().apply(ret));
    }

    private String classMethod()
    {
        return String.format(
            "%s.%s"
            , clazz.getName()
            , method.getName()
        );
    }

    private <T> String actual1(T v)
    {
        if (Objects.isNull(before))
        {
            return String.format(
                "%s"
                , v
            );
        }

        return String.format(
            "%s(%s)"
            , before.classMethod()
            , v
        );
    }

    private <T> Function1<T, String> actual1()
    {
        return (v) -> String.format(
            "%s(%s)"
            , classMethod()
            , actual1(v)
        );
    }

    private <T1, T2> Function2<T1, T2, R> expected2()
    {
        // TODO why doesn't CheckedFunction2 combined with unchecked work?
        return (v1, v2) -> invoke(method, clazz, v1, v2);

//        final Function2<T1, T2, Object> invoke = (v1, v2) -> invoke(method, clazz, v1, v2);
//        return invoke.andThen(ret -> Unchecked.<ParamType<Object>>cast(returns).toLiteral().apply(ret));
    }

    private <T1, T2> Function2<T1, T2, String> actual2()
    {
        return (v1, v2) -> String.format(
            "%s.%s(%s, %s)"
            , clazz.getName()
            , method.getName()
            , v1
            , v2
//            , Unchecked.<ParamType<T1>>cast(inputs.get(0)).toLiteral().apply(v1)
//            , Unchecked.<ParamType<T1>>cast(inputs.get(0)).toHex(v1)
//            , Unchecked.<ParamType<T2>>cast(inputs.get(1)).toLiteral().apply(v2)
//            , Unchecked.<ParamType<T2>>cast(inputs.get(1)).toHex(v2)
        );
    }

    static <R> StaticMethod<R> of(Recipe.StaticMethod recipe)
    {
        final var type = recipe.type();
        final var methodParamsLookup = Arrays.stream(type.getMethods())
            .filter(m -> m.getName().equals(recipe.methodName()))
            .map(Method::getParameterTypes)
            .findFirst();

        if (methodParamsLookup.isEmpty())
            throw new RuntimeException("Not found");

        final var methodParams = methodParamsLookup.get();

        final var method = CheckedFunction2
            .lift(CheckedFunction2.<String, Class<?>[], Method>of(type::getMethod))
            .apply(recipe.methodName(), methodParams)
            .getOrElseThrow(() -> new RuntimeException(""));

        final List<ParamType<?>> params = List
            .ofAll(Arrays.stream(methodParams))
            .map(ParamType::of);

        return new StaticMethod<>(
            method
            , type
            , params.map(Literal::of) // TODO eventually they might be actual expressions
            , params.map(p -> Values.values(p.arbitrary()).iterator())
            , ParamType.of(method.getReturnType())
            , Objects.isNull(recipe.before()) ? null : StaticMethod.of(recipe.before())
        );
    }

    private static <T> T invoke(Method m, Object obj, Object... args)
    {
        try
        {
            return (T) m.invoke(obj, args);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Expects<R> expects()
    {
        return switch (inputs.length())
        {
            case 1 -> expects1();
            case 2 -> expects2();
            default -> throw new IllegalStateException("Unexpected value: " + inputs.length());
        };
    }

    private Expects<R> expects1()
    {
        final Object value = inputValues.get(0).next();
        return new Expects<>(
            expected1().apply(value)
            , actual1().apply(value)
        );
    }

    private Expects<R> expects2()
    {
        final Object v1 = inputValues.get(0).next();
        final Object v2 = inputValues.get(1).next();
        return new Expects<>(
            expected2().apply(v1, v2)
            , actual2().apply(v1, v2)
        );
    }
}
