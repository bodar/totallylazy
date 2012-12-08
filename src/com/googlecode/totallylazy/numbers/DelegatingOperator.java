package com.googlecode.totallylazy.numbers;

public class DelegatingOperator implements Operators<Number> {
    private final Operators<Number> operators;

    public DelegatingOperator(Operators<Number> operators) {this.operators = operators;}

    @Override
    public int priority() {
        return operators.priority() + 1;
    }

    @Override
    public Number negate(Number value) {
        return operators.negate(number(value));
    }

    private Number number(Number value) {
        return value instanceof DelegatingNumber ? ((DelegatingNumber) value).value() : value;
    }

    @Override
    public Number increment(Number value) {
        return operators.increment(number(value));
    }

    @Override
    public Number decrement(Number value) {
        return operators.decrement(number(value));
    }

    @Override
    public boolean isZero(Number value) {
        return operators.isZero(number(value));
    }

    @Override
    public boolean isPositive(Number value) {
        return operators.isPositive(number(value));
    }

    @Override
    public boolean isNegative(Number value) {
        return operators.isNegative(number(value));
    }

    @Override
    public boolean equalTo(Number x, Number y) {
        return operators.equalTo(number(x), number(y));
    }

    @Override
    public boolean lessThan(Number x, Number y) {
        return operators.lessThan(number(x), number(y));
    }

    @Override
    public Number add(Number x, Number y) {
        return operators.add(number(x), number(y));
    }

    @Override
    public Number multiply(Number x, Number y) {
        return operators.multiply(number(x), number(y));
    }

    @Override
    public Number divide(Number x, Number y) {
        return operators.divide(number(x), number(y));
    }

    @Override
    public Number quotient(Number x, Number y) {
        return operators.quotient(number(x), number(y));
    }

    @Override
    public Number remainder(Number x, Number y) {
        return operators.remainder(number(x), number(y));
    }

    @Override
    public Class<Number> forClass() {
        return operators.forClass();
    }


}
