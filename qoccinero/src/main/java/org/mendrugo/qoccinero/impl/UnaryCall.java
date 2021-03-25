package org.mendrugo.qoccinero.impl;

record UnaryCall(String operator, Expression expr) implements Expression {}
