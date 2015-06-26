package com.googlecode.totallylazy.functions;

import java.util.HashMap;
import java.util.Map;

public final class CountingFunction<T,R> implements Function1<T,R> {
    private final Map<T, Integer> count = new HashMap<T, Integer>();
    private final Function1<? super T, ? extends R> callable;

    private CountingFunction(Function1<? super T, ? extends R> callable) {
        this.callable = callable;
    }

    public final R call(T t) throws Exception {
        count.put(t, count(t) + 1);
        return callable.call(t);
    }

    public final int count(T t) {
        if(!count.containsKey(t)){
            count.put(t, 0);
        }
        return count.get(t);
    }

    public static <T,R> CountingFunction<T,R> counting(Function1<? super T,? extends R> callable) {
        return new CountingFunction<T,R>(callable);
    }
}
