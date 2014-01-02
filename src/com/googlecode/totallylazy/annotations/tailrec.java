package com.googlecode.totallylazy.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** Used by <a href="https://code.google.com/p/jcompilo/">jcompilo</a> to do tail call optimisation */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface tailrec {}
