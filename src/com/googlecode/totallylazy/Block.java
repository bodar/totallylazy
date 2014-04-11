package com.googlecode.totallylazy;

import java.util.function.Consumer;

public interface Block<T> extends Function<T, Void>, Consumer<T> {
    @Override
    default Void call(T t) throws Exception {
        execute(t);
        return Runnables.VOID;
    }

    @Override
    default void accept(T t) {
        apply(t);
    }

    void execute(T t) throws Exception;

    static <T> Block<T> block(Consumer<? super T> callable) {
        return callable::accept;
    }

}
