package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.CheckedFunction1;
import io.vavr.CheckedFunction2;
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
            case 2 -> MethodSpecs.toMethodSpec2(params.get(0), params.get(1), expected2(), actual2(), methodName);
            default -> throw new IllegalStateException("Unexpected value: " + params.length());
        };
    }

    private <T1, T2> Function2<T1, T2, String> expected2()
    {
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
        final var clazz = lookupClass(recipe.className());
        final var methodParamsLookup = Arrays.stream(clazz.getMethods())
            .filter(m -> m.getName().equals(recipe.methodName()))
            .map(Method::getParameterTypes)
            .findFirst();

        if (methodParamsLookup.isEmpty())
            throw new RuntimeException("Not found");

        final var methodParams = methodParamsLookup.get();
        // final var method = classMethod(methodParams, recipe.methodName(), clazz);
        final var method = CheckedFunction2
            .lift(CheckedFunction2.<String, Class<?>[], Method>of(clazz::getMethod))
            .apply(recipe.methodName(), methodParams)
            .getOrElseThrow(() -> new RuntimeException(""));

        return new StaticMethod(
            method
            , clazz
            , List.ofAll(Arrays.stream(methodParams)).map(ParamType::of)
            , ParamType.of(method.getReturnType())
        );
    }

    private static Class<?> lookupClass(String className)
    {
        try
        {
            return Class.forName(className);
        }
        catch (ClassNotFoundException e)
        {
            final Class<?> javaClass = tryLookupJavaClass(className);
            if (javaClass == null)
            {
                throw new RuntimeException(e);
            }

            return javaClass;
        }
    }

    private static Class<?> tryLookupJavaClass(String className)
    {
        // TODO there's only one imported package
        return List.of("java.lang")
            .map(prefix -> String.format("%s.%s", prefix, className))
            .map(CheckedFunction1.lift(Class::forName))
            .find(Option::isDefined)
            .map(Option::get)
            .getOrNull();
    }

    private static Method classMethod(Class<?>[] paramTypes, String methodName, Class<?> clazz)
    {
        try
        {
            return clazz.getMethod(methodName, paramTypes);
        }
        catch (NoSuchMethodException e)
        {
            throw new RuntimeException(e);
        }
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
