package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
import io.vavr.Function1;
import io.vavr.Function2;
import io.vavr.collection.List;
import io.vavr.control.Option;

import java.lang.reflect.Method;
import java.util.Arrays;

record StaticMethod(
    Method method
    , Class<?> clazz
    , List<ParamType<?>> params
    , ParamType<?> returns
)
{
    MethodSpec toMethodSpec()
    {
        final var methodName = String.format(
            "%s_%s"
            , clazz.getName().replace('.', '_')
            , this.method.getName()
        );

        return switch (params.length())
        {
            case 1 -> MethodSpecs.toMethodSpec1(params.get(0), expected1(), actual1(), methodName);
            case 2 -> MethodSpecs.toMethodSpec2(params.get(0), params.get(1), expected2(), actual2(), methodName);
            default -> throw new IllegalStateException("Unexpected value: " + params.length());
        };
    }

    private <T> Function1<T, String> expected1()
    {
        // TODO why doesn't CheckedFunction2 combined with unchecked work?
        final Function1<T, Object> invoke = v -> invoke(method, clazz, v);
        return invoke.andThen(ret -> Unchecked.<ParamType<Object>>cast(returns).toLiteral().apply(ret));
    }

    private <T> Function1<T, String> actual1()
    {
        return (v) -> String.format(
            "%s.%s(%s /* %s */)"
            , clazz.getName()
            , method.getName()
            , Unchecked.<ParamType<T>>cast(params.get(0)).toLiteral().apply(v)
            , Unchecked.<ParamType<T>>cast(params.get(0)).toHex(v)
        );
    }

    private <T1, T2> Function2<T1, T2, String> expected2()
    {
        // TODO why doesn't CheckedFunction2 combined with unchecked work?
        final Function2<T1, T2, Object> invoke = (v1, v2) -> invoke(method, clazz, v1, v2);
        return invoke.andThen(ret -> Unchecked.<ParamType<Object>>cast(returns).toLiteral().apply(ret));
    }

    private <T1, T2> Function2<T1, T2, String> actual2()
    {
        return (v1, v2) -> String.format(
            "%s.%s(%s /* %s */, %s /* %s */)"
            , clazz.getName()
            , method.getName()
            , Unchecked.<ParamType<T1>>cast(params.get(0)).toLiteral().apply(v1)
            , Unchecked.<ParamType<T1>>cast(params.get(0)).toHex(v1)
            , Unchecked.<ParamType<T2>>cast(params.get(1)).toLiteral().apply(v2)
            , Unchecked.<ParamType<T2>>cast(params.get(1)).toHex(v2)
        );
    }

//    private <T1, T2> MethodSpec toMethodSpec2(MethodSpec.Builder method, Function2<T1, T2, String> expected, Function2<T1, T2, String> actual)
//    {
//        final var values = ParamType.values(
//            Unchecked.<ParamType<T1>>cast(params.get(0))
//            , Unchecked.<ParamType<T2>>cast(params.get(1))
//        );
//
//        values
//            .map(tuple2 -> toCode(tuple2._1, tuple2._2, expected, actual))
//            .append(CodeBlock.of("putchar('\\n'); \n"))
//            .forEach(method::addCode);
//
//        return method.build();
//    }

//    private <T1, T2> CodeBlock toCode(T1 v1, T2 v2, Function2<T1, T2, String> expected, Function2<T1, T2, String> actual)
//    {
//        return CodeBlock.of(
//            "putchar($L == $L ? '.' : 'F');\n"
//            , expected.apply(v1, v2)
//            , actual.apply(v1, v2)
//        );
//    }

    static StaticMethod of(StaticMethodRecipe recipe)
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

        return new StaticMethod(
            method
            , type
            , List.ofAll(Arrays.stream(methodParams)).map(ParamType::of)
            , ParamType.of(method.getReturnType())
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
}
