package com.googlecode.totallylazy.numbers;

public final class ShortOperators implements Operators<Short> {
    public static ShortOperators Instance = new ShortOperators();

    private ShortOperators() {
    }

    @Override
    public Number absolute(Short value) {
        return isNegative(value) ? negate(value) : value ;
    }

    @Override
    public Number negate(Short value) {
        if (value == Short.MIN_VALUE) {
            return -value;
        }
        return (short) (-value);
    }

    @Override
    public Number increment(Short value) {
        final int result = value + 1;
        if (result > Short.MAX_VALUE) {
            return result;
        } else {
            return (short) result;
        }
    }

    @Override
    public Number decrement(Short value) {
        final int result = value - 1;
        if (result < Short.MIN_VALUE) {
            return result;
        } else {
            return (short) result;
        }
    }

    @Override
    public boolean isZero(Short value) {
        return value == 0;
    }

    @Override
    public boolean isPositive(Short value) {
        return value > 0;
    }

    @Override
    public boolean isNegative(Short value) {
        return value < 0;
    }

    @Override
    public boolean equalTo(Number x, Number y) {
        return x.shortValue() == y.shortValue();
    }

    @Override
    public boolean lessThan(Number x, Number y) {
        return x.shortValue() < y.shortValue();
    }

    @Override
    public Number add(Number x, Number y) {
        final int result = x.shortValue() + y.shortValue();
        if (result <= Short.MAX_VALUE && result >= Short.MIN_VALUE) {
            return (short) result;
        } else {
            return result;
        }
    }

    @Override
    public Number multiply(Number x, Number y) {
        final int result = x.intValue() * y.intValue();
        if (result <= Short.MAX_VALUE && result >= Short.MIN_VALUE) {
            return (short) result;
        } else {
            return result;
        }
    }

    @Override
    public Number divide(Number x, Number y) {
        return IntegerOperators.Instance.divide(x, y);
    }

    @Override
    public Number quotient(Number x, Number y) {
        final int result = x.shortValue() / y.shortValue();
        if (result <= Short.MAX_VALUE && result >= Short.MIN_VALUE) {
            return (short) result;
        } else {
            return result;
        }
    }

    @Override
    public Number remainder(Number x, Number y) {
        final int result = x.shortValue() % y.shortValue();
        if (result <= Short.MAX_VALUE && result >= Short.MIN_VALUE) {
            return (short) result;
        } else {
            return result;
        }
    }

    @Override
    public Class<Short> forClass() {
        return Short.class;
    }

    @Override
    public int priority() {
        return -1;
    }
}
