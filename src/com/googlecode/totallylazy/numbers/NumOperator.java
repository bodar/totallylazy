package com.googlecode.totallylazy.numbers;

public class NumOperator implements Operators<Number> {
    private final Operators<Number> operators;

    public NumOperator(Operators<Number> operators) {this.operators = operators;}

    @Override
    public int priority() {
        return operators.priority() + 1;
    }

    @Override
    public Number absolute(Number value) {
        return operators.absolute(raw(value));
    }

    @Override
    public Number negate(Number value) {
        return operators.negate(raw(value));
    }

    private Number raw(Number value) {
        return value instanceof Num ? ((Num) value).value() : value;
    }

    @Override
    public Number increment(Number value) {
        return operators.increment(raw(value));
    }

    @Override
    public Number decrement(Number value) {
        return operators.decrement(raw(value));
    }

    @Override
    public boolean isZero(Number value) {
        return operators.isZero(raw(value));
    }

    @Override
    public boolean isPositive(Number value) {
        return operators.isPositive(raw(value));
    }

    @Override
    public boolean isNegative(Number value) {
        return operators.isNegative(raw(value));
    }

    @Override
    public boolean equalTo(Number x, Number y) {
        return operators.equalTo(raw(x), raw(y));
    }

    @Override
    public boolean lessThan(Number x, Number y) {
        return operators.lessThan(raw(x), raw(y));
    }

    @Override
    public Number add(Number x, Number y) {
        return operators.add(raw(x), raw(y));
    }

    @Override
    public Number multiply(Number x, Number y) {
        return operators.multiply(raw(x), raw(y));
    }

    @Override
    public Number divide(Number x, Number y) {
        return operators.divide(raw(x), raw(y));
    }

    @Override
    public Number quotient(Number x, Number y) {
        return operators.quotient(raw(x), raw(y));
    }

    @Override
    public Number remainder(Number x, Number y) {
        return operators.remainder(raw(x), raw(y));
    }

    @Override
    public Class<Number> forClass() {
        return operators.forClass();
    }


}
