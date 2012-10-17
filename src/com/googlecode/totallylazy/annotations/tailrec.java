package com.googlecode.totallylazy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// This used by compilo to do tail call optimisation
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface tailrec {}
