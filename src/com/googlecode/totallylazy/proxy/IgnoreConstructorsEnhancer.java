package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.Enhancer;

import java.util.List;

class IgnoreConstructorsEnhancer extends Enhancer {
    @Override
    protected void filterConstructors(Class sc, List constructors) {
    }
}
