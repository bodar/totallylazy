package com.googlecode.totallylazy;

import com.googlecode.totallylazy.annotations.multimethod;

public interface Value<T> {
    T value();

    abstract class Type<T> extends Eq implements Value<T> {
        protected final T value;

        protected Type(T value) {this.value = value;}

        @Override
        public T value() {
            return value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public String toString() {
            return getClass().getSimpleName() + "(" + value.toString() + ")";
        }

        @multimethod
        public boolean equals(Type<T> type) {
            return type.value.equals(value);
        }
    }
}
