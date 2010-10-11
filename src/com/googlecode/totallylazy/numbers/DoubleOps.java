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

final class DoubleOps implements Ops {
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
        return this;
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
        return x.doubleValue() == 0;
    }

    public boolean isPos(Number x) {
        return x.doubleValue() > 0;
    }

    public boolean isNeg(Number x) {
        return x.doubleValue() < 0;
    }

    final public Number add(Number x, Number y) {
        return x.doubleValue() + y.doubleValue();
    }

    final public Number multiply(Number x, Number y) {
        return x.doubleValue() * y.doubleValue();
    }

    public Number divide(Number x, Number y) {
        return x.doubleValue() / y.doubleValue();
    }

    public Number quotient(Number x, Number y) {
        return Numbers.quotient(x.doubleValue(), y.doubleValue());
    }

    public Number remainder(Number x, Number y) {
        return Numbers.remainder(x.doubleValue(), y.doubleValue());
    }

    public boolean equiv(Number x, Number y) {
        return x.doubleValue() == y.doubleValue();
    }

    public boolean lt(Number x, Number y) {
        return x.doubleValue() < y.doubleValue();
    }

    //public Number subtract(Number x, Number y);

    final public Number negate(Number x) {
        return -x.doubleValue();
    }

    public Number inc(Number x) {
        return x.doubleValue() + 1;
    }

    public Number dec(Number x) {
        return x.doubleValue() - 1;
    }
}
