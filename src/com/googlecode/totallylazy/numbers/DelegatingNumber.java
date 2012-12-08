package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Value;

public abstract class DelegatingNumber extends Number implements Value<Number> {
    private final Number number;

    protected DelegatingNumber(Number number) {
        this.number = number;
    }

    public Number value() {
        return number;
    }

    @Override
    public int intValue() {
        return number.intValue();
    }

    @Override
    public long longValue() {
        return number.longValue();
    }

    @Override
    public float floatValue() {
        return number.floatValue();
    }

    @Override
    public double doubleValue() {
        return number.doubleValue();
    }

    @Override
    public byte byteValue() {
        return number.byteValue();
    }

    @Override
    public short shortValue() {
        return number.shortValue();
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return number.equals(obj);
    }

    @Override
    public String toString() {
        return number.toString();
    }
}
