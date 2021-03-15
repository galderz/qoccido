package org.mendrugo.qoccinero.impl;

public sealed interface Expression permits
    BinaryCall
    , Constant
    , Hole
    , StaticCall
    , UnaryCall
{}
