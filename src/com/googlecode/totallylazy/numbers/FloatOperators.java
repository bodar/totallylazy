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
    public Class<Float> forClass() {
        return Float.class;
    }

    public final Number negate(Float value) {
        return -value;
    }

    public final Number increment(Float value) {
        return value + 1;
    }

    public final Number decrement(Float value) {
        return value - 1;
    }

    public final boolean isZero(Float value) {
        return value == 0;
    }

    public final boolean isPositive(Float value) {
        return value > 0;
    }

    public final boolean isNegative(Float value) {
        return value < 0;
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
