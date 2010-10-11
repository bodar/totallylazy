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

final class FloatOps implements Ops {
    public Ops combine(Ops y) {
        return y.opsWith(this);
    }

    final public Ops opsWith(IntegerOps x) {
        return this;
    }

    final public Ops opsWith(LongOps x) {
        return this;
    }

    final public Ops opsWith(FloatOps x) {
        return this;
    }

    final public Ops opsWith(DoubleOps x) {
        return Numbers.DOUBLE_OPS;
    }

    final public Ops opsWith(RatioOps x) {
        return this;
    }

    final public Ops opsWith(BigIntegerOps x) {
        return this;
    }

    final public Ops opsWith(BigDecimalOps x) {
        return this;
    }

    public boolean isZero(Number x) {
        return x.floatValue() == 0;
    }

    public boolean isPositive(Number x) {
        return x.floatValue() > 0;
    }

    public boolean isNegative(Number x) {
        return x.floatValue() < 0;
    }

    final public Number add(Number x, Number y) {
        return x.floatValue() + y.floatValue();
    }

    final public Number multiply(Number x, Number y) {
        return x.floatValue() * y.floatValue();
    }

    public Number divide(Number x, Number y) {
        return x.floatValue() / y.floatValue();
    }

    public Number quotient(Number x, Number y) {
        return Numbers.quotient(x.doubleValue(), y.doubleValue());
    }

    public Number remainder(Number x, Number y) {
        return Numbers.remainder(x.doubleValue(), y.doubleValue());
    }

    public boolean equalTo(Number x, Number y) {
        return x.floatValue() == y.floatValue();
    }

    public boolean lessThan(Number x, Number y) {
        return x.floatValue() < y.floatValue();
    }

    final public Number negate(Number x) {
        return -x.floatValue();
    }

    public Number increment(Number x) {
        return x.floatValue() + 1;
    }

    public Number decrement(Number x) {
        return x.floatValue() - 1;
    }
}