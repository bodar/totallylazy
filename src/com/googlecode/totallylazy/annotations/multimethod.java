package com.googlecode.totallylazy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method that will be dispatched using runtime polymorphic dispatch.
 * As this is done at runtime, the annotated method may appear to have no usages.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface multimethod {
}
