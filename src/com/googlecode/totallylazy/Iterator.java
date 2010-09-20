package com.googlecode.totallylazy;

public abstract class Iterator<T> implements java.util.Iterator<T>{
    public void remove() {
        throw new UnsupportedOperationException();
    }

    public static <T> void foreach(java.util.Iterator<T> iterator, Runnable1<T> runnable) {
        while (iterator.hasNext()){
            runnable.run(iterator.next());
        }
    }

    public static <T,S> Iterator<S> map(java.util.Iterator<T> iterator, Callable1<T,S> callable) {
        return new MapIterator<T,S>(iterator, callable);
    }

    public static <T,S> Iterator<S> flaMap(java.util.Iterator<T> iterator, Callable1<T, Iterable<S>> callable) {
        return new FlatMapIterator<T,S>(iterator, callable);
    }

    public static <T> Iterator<T> filter(java.util.Iterator<T> iterator, Predicate<T> predicate) {
        return new FilterIterator<T>(iterator, predicate);
    }
}
