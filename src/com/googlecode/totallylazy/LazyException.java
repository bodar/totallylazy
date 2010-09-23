package com.googlecode.totallylazy;

public class LazyException extends RuntimeException {
    public LazyException(Throwable cause) {
        super(cause);
    }

    public <E extends Exception> E unwrap(Class<E> exception) throws E{
        return unwrap(this, exception);
    }

    public static  <E extends Exception> E unwrap(RuntimeException e, Class<E> exception) throws E{
        final Throwable theCause = e.getCause();
        if(theCause.getClass().equals(exception)){
            return (E) theCause;
        }
        if(theCause instanceof RuntimeException){
            throw (RuntimeException) theCause;
        }
        throw e;
    }
}
