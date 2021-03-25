package org.mendrugo.qoccinero.impl;

sealed interface Expression permits
    BinaryCall
    , Constant
    , Hole
    , StaticCall
    , UnaryCall
{}
