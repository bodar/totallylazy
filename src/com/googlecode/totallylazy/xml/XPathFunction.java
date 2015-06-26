package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.functions.Function1;

@java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface XPathFunction {
    java.lang.String value();

    class functions {
        public static Function1<XPathFunction, String> value() {
            return XPathFunction::value;
        }
    }
}