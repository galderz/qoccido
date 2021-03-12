package org.mendrugo.qoccinero.impl;

public sealed interface Expression permits
    BinaryCall
    , Constant
    , Hole
    , StaticCall
{
//    static boolean isHole(Expression expr)
//    {
//        return expr instanceof Hole;
//    }
//
//    static boolean isStaticCall(Expression expr)
//    {
//        return expr instanceof StaticCall;
//    }
}

