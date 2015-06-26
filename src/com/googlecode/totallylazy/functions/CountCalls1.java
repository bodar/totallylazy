package com.googlecode.totallylazy.functions;

import java.util.HashMap;
import java.util.Map;

public final class CountCalls1<T,R> implements Function1<T,R> {
    private final Map<T, Integer> count = new HashMap<T, Integer>();
    private final Function1<? super T, ? extends R> callable;

    private CountCalls1(Function1<? super T, ? extends R> callable) {
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

    public static <T,R> CountCalls1<T,R> counting(Function1<? super T,? extends R> callable) {
        return new CountCalls1<T,R>(callable);
    }
}
