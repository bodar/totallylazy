package com.googlecode.totallylazy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Marks a runtime polymorphic dispatch method see {@link com.googlecode.totallylazy.multi} }*/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface multimethod {
}
