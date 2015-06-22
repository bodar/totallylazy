package com.googlecode.totallylazy;

public interface Block<T> extends Function1<T, Void> {
    @Override
    default Void call(T t) throws Exception {
        execute(t);
        return Runnables.VOID;
    }

    void execute(T t) throws Exception;

    // Use with lambdas
    static <T> Block<T> block(Block<T> block){
        return block;
    }
}
