package com.googlecode.totallylazy.numbers;

public abstract class DelegatingNumber extends Number {
    private final Number number;

    protected DelegatingNumber(Number number) {
        this.number = number;
    }

    public Number number() {
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
}
