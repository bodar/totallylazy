package com.googlecode.totallylazy;

public interface Constructable<T, Self extends Constructable<T, Self>> {
    Self cons(T head);

    <C extends Constructable<T, C>> C join(C rest);
}
