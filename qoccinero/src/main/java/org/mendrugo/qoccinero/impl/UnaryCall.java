package org.mendrugo.qoccinero.impl;

public record UnaryCall(String operator, Expression expr) implements Expression {}
