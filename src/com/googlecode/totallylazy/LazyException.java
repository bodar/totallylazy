package com.googlecode.totallylazy;

import java.lang.reflect.InvocationTargetException;

public class LazyException extends RuntimeException {
    public LazyException(Throwable cause) {
        this(null, cause);
    }
    public LazyException(String message, Throwable cause) {
        super(message, unwrapLazy(cause));
    }

    private static Throwable unwrapLazy(Throwable cause) {
        if(cause instanceof LazyException){
            return cause.getCause();
        }
        if(cause instanceof InvocationTargetException){
            return cause.getCause();
        }
        return cause;
    }

    public <E extends Exception> E unwrap(Class<E> exception) throws E{
        return unwrap(this, exception);
    }

    public static  <E extends Exception> E unwrap(RuntimeException e, Class<E> exception) throws E{
        final Throwable theCause = e.getCause();
        if(theCause.getClass().equals(exception)){
            return (E) theCause;
        }
        throw e;
    }
}
