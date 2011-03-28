package com.googlecode.totallylazy.records;

import com.googlecode.totallylazy.Callable2;

import static com.googlecode.totallylazy.numbers.Numbers.increment;

public class CountNotNull implements Callable2<Object, Object, Number> {
    private Number count = 1;

    public Number call(Object a, Object b) throws Exception {
        if(b != null){
            count = increment(count);
        }
        return count;
    }

    public static Callable2<Object, Object, Number> count() {
        return new CountNotNull();
    }

}
