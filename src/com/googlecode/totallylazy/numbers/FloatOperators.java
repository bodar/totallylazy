package com.googlecode.totallylazy.numbers;

/**
 * Copyright (c) Rich Hickey. All rights reserved.
 * The use and distribution terms for this software are covered by the
 * Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 * which can be found in the file epl-v10.html at the root of this distribution.
 * By using this software in any fashion, you are agreeing to be bound by
 * the terms of this license.
 * You must not remove this notice, or any other, from this software.
 */

/* rich Mar 31, 2008 */

public final class FloatOperators implements Operators<Float> {
    public final Operators combine(Operators y) {
        return y.opsWith(this);
    }

    public final Operators opsWith(IntegerOperators x) {
        return this;
    }

    public final Operators opsWith(LongOperators x) {
        return this;
    }

    public final Operators opsWith(FloatOperators x) {
        return this;
    }

    public final Operators opsWith(DoubleOperators x) {
        return Numbers.DOUBLE_OPS;
    }

    public final Operators opsWith(RatioOperators x) {
        return this;
    }

    public final Operators opsWith(BigIntegerOperators x) {
        return this;
    }

    public final Operators opsWith(BigDecimalOperators x) {
        return this;
    }

    public final Number negate(Float x) {
        return -x;
    }

    public final Number increment(Float x) {
        return x + 1;
    }

    public final Number decrement(Float x) {
        return x - 1;
    }

    public final boolean isZero(Float x) {
        return x == 0;
    }

    public final boolean isPositive(Float x) {
        return x > 0;
    }

    public final boolean isNegative(Float x) {
        return x < 0;
    }

    public final boolean equalTo(Number x, Number y) {
        return x.floatValue() == y.floatValue();
    }

    public final boolean lessThan(Number x, Number y) {
        return x.floatValue() < y.floatValue();
    }

    public final Number add(Number x, Number y) {
        return x.floatValue() + y.floatValue();
    }

    public final Number multiply(Number x, Number y) {
        return x.floatValue() * y.floatValue();
    }

    public final Number divide(Number x, Number y) {
        return x.floatValue() / y.floatValue();
    }

    public final Number quotient(Number x, Number y) {
        return Numbers.quotient(x.doubleValue(), y.doubleValue());
    }

    public final Number remainder(Number x, Number y) {
        return Numbers.remainder(x.doubleValue(), y.doubleValue());
    }
}
