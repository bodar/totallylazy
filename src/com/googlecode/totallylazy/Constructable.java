package com.googlecode.totallylazy;

public interface Constructable<T, Self extends Constructable<T, Self>> {
    Self cons(T head);

    <C extends Constructable<T, C>> C join(C rest);

    class functions {
        public static <T, Self extends Constructable<T, Self>> Function2<Self, T, Self> cons() {
            return new Function2<Self, T, Self>() {
                @Override
                public Self call(Self set, T t) throws Exception {
                    return set.cons(t);
                }
            };
        }
    }
}
