package com.googlecode.totallylazy;

public class LazyException extends RuntimeException {
    public LazyException(Throwable cause) {
        super(cause);
    }

    public <E extends Exception> void unwrap(Class<E> exception) throws E{
        unwrap(this, exception);
    }

    public static  <E extends Exception> void unwrap(RuntimeException e, Class<E> exception) throws E{
        final Throwable theCause = e.getCause();
        if(theCause.getClass().equals(exception)){
            throw (E) theCause;
        }
        if(theCause instanceof RuntimeException){
            throw (RuntimeException) theCause;
        }
        throw e;
    }
}
