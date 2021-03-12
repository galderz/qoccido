package org.mendrugo.qoccinero.impl;

public record BinaryCall(Expression left, String operator, Expression right) implements Expression {}
