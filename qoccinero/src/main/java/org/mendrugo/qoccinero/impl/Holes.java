package org.mendrugo.qoccinero.impl;

import io.vavr.collection.List;

final class Holes
{
    static List<Class<?>> types(Expression expr)
    {
        return types(expr, List.of());
    }

    private static List<Class<?>> types(Expression expr, List<Class<?>> list)
    {
        if (expr instanceof BinaryCall binaryCall)
        {
            if (binaryCall.left() instanceof Hole && binaryCall.right() instanceof Hole)
            {
                return list.append(int.class).append(int.class);
            }

            if (binaryCall.left() instanceof Hole || binaryCall.right() instanceof Hole)
            {
                return list.append(int.class);
            }

            return types(binaryCall.right(), types(binaryCall.left(), list));
        }

        if (expr instanceof Constant)
        {
            return list;
        }

        if (expr instanceof StaticCall staticCall)
        {
            var paramTypes = Reflection.parameterTypes(staticCall.method())
                .zip(staticCall.params())
                .filter(reflectAndExpr -> reflectAndExpr._2 instanceof Hole)
                .map(reflectAndExpr -> reflectAndExpr._1);

            if (paramTypes.isEmpty())
            {
                return staticCall.params().flatMap(paramExpr -> types(paramExpr, list));
            }

            return list.appendAll(paramTypes);
        }

        if (expr instanceof UnaryCall unaryCall)
        {
            return types(unaryCall.expr(), list);
        }

        throw new RuntimeException("NYI");
    }
}
