package com.googlecode.totallylazy.xml;

import com.googlecode.totallylazy.Function;

@java.lang.annotation.Target(java.lang.annotation.ElementType.METHOD)
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface XPathFunction {
    java.lang.String value();

    class functions {
        public static Function<XPathFunction, String> value() {
            return new Function<XPathFunction, String>() {
                @Override
                public String call(XPathFunction annotation) throws Exception {
                    return annotation.value();
                }
            };
        }
    }
}