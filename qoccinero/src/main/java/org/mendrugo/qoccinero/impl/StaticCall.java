package org.mendrugo.qoccinero.impl;

import io.vavr.collection.List;

import java.lang.reflect.Method;

record StaticCall(Method method, Class<?> type, List<Expression> params) implements Expression {}
