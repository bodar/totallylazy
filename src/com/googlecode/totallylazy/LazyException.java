package com.googlecode.totallylazy;

import java.lang.reflect.InvocationTargetException;

public class LazyException extends RuntimeException {
    static final long serialVersionUID = -6664897190745766939L;
    public static LazyException lazyException(final Throwable cause){
        return lazyException(null, cause);
    }

    public static LazyException lazyException(String message, final Throwable cause){
        if(cause instanceof RuntimeException){
            throw (RuntimeException)cause;
        }
        return new LazyException(message, cause);
    }

    private LazyException(String message, Throwable cause) {
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
            return exception.cast(theCause);
        }
        throw e;
    }
}
