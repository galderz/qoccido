package org.mendrugo.qoccinero;

import com.squareup.javapoet.MethodSpec;
import io.vavr.collection.List;

import javax.lang.model.element.Modifier;

final class Helpers
{
    static List<MethodSpec> methods()
    {
        return binaryMethods(">", "isGreater")
            .appendAll(binaryMethods("<", "isLess"));
    }

    private static List<MethodSpec> binaryMethods(String operator, String methodName)
    {
        return List.of(
            binaryMethod(operator, double.class, methodName)
            , binaryMethod(operator, float.class, methodName)
            , binaryMethod(operator, long.class, methodName)
        );
    }

    private static MethodSpec binaryMethod(String operator, Class<?> type, String name)
    {
        return MethodSpec.methodBuilder(name)
            .addModifiers(Modifier.STATIC)
            .returns(boolean.class)
            .addParameter(type, "a")
            .addParameter(type, "b")
            .addStatement("return a $L b", operator)
            .build();
    }
}
