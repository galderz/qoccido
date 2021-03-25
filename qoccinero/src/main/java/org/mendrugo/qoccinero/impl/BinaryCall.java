package org.mendrugo.qoccinero.impl;

record BinaryCall(Expression left, String operator, Expression right) implements Expression {}
