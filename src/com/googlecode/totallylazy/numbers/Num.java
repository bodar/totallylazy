package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Value;

public class Num extends Number implements Value<Number> {
    private final Number value;

    protected Num(Number value) {
        this.value = value;
    }

    public static Num num(Number number) {return new Num(number);}

    public Number value() {
        return value;
    }

    @Override
    public int intValue() {
        return value.intValue();
    }

    @Override
    public long longValue() {
        return value.longValue();
    }

    @Override
    public float floatValue() {
        return value.floatValue();
    }

    @Override
    public double doubleValue() {
        return value.doubleValue();
    }

    @Override
    public byte byteValue() {
        return value.byteValue();
    }

    @Override
    public short shortValue() {
        return value.shortValue();
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return value.equals(obj);
    }

    @Override
    public String toString() {
        return value.toString();
    }

    public Num add(Number other) {
        return num(Numbers.add(value, other));
    }

    public Num subtract(Number other) {
        return num(Numbers.subtract(value, other));
    }

    public Num multiply(Number other) {
        return num(Numbers.multiply(value, other));
    }

    public Num divide(Number other) {
        return num(Numbers.divide(value, other));
    }

    public boolean isZero() {
        return Numbers.isZero(value);
    }
}
