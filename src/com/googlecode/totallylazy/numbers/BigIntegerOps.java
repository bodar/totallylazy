package com.googlecode.totallylazy.numbers;
/**
 *   Copyright (c) Rich Hickey. All rights reserved.
 *   The use and distribution terms for this software are covered by the
 *   Eclipse Public License 1.0 (http://opensource.org/licenses/eclipse-1.0.php)
 *   which can be found in the file epl-v10.html at the root of this distribution.
 *   By using this software in any fashion, you are agreeing to be bound by
 * 	 the terms of this license.
 *   You must not remove this notice, or any other, from this software.
 **/

/* rich Mar 31, 2008 */

import java.math.BigInteger;

final class BigIntegerOps implements Ops {
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
        return Numbers.FLOAT_OPS;
    }

    final public Ops opsWith(DoubleOps x) {
        return Numbers.DOUBLE_OPS;
    }

    final public Ops opsWith(RatioOps x) {
        return Numbers.RATIO_OPS;
    }

    final public Ops opsWith(BigIntegerOps x) {
        return this;
    }

    final public Ops opsWith(BigDecimalOps x) {
        return Numbers.BIGDECIMAL_OPS;
    }

    public boolean isZero(Number x) {
        BigInteger bx = Numbers.toBigInteger(x);
        return bx.signum() == 0;
    }

    public boolean isPos(Number x) {
        BigInteger bx = Numbers.toBigInteger(x);
        return bx.signum() > 0;
    }

    public boolean isNeg(Number x) {
        BigInteger bx = Numbers.toBigInteger(x);
        return bx.signum() < 0;
    }

    final public Number add(Number x, Number y) {
        return Numbers.reduce(Numbers.toBigInteger(x).add(Numbers.toBigInteger(y)));
    }

    final public Number multiply(Number x, Number y) {
        return Numbers.reduce(Numbers.toBigInteger(x).multiply(Numbers.toBigInteger(y)));
    }

    public Number divide(Number x, Number y) {
        return Numbers.divide(Numbers.toBigInteger(x), Numbers.toBigInteger(y));
    }

    public Number quotient(Number x, Number y) {
        return Numbers.toBigInteger(x).divide(Numbers.toBigInteger(y));
    }

    public Number remainder(Number x, Number y) {
        return Numbers.toBigInteger(x).remainder(Numbers.toBigInteger(y));
    }

    public boolean equiv(Number x, Number y) {
        return Numbers.toBigInteger(x).equals(Numbers.toBigInteger(y));
    }

    public boolean lt(Number x, Number y) {
        return Numbers.toBigInteger(x).compareTo(Numbers.toBigInteger(y)) < 0;
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        return Numbers.toBigInteger(x).negate();
    }

    public Number inc(Number x) {
        BigInteger bx = Numbers.toBigInteger(x);
        return Numbers.reduce(bx.add(BigInteger.ONE));
    }

    public Number dec(Number x) {
        BigInteger bx = Numbers.toBigInteger(x);
        return Numbers.reduce(bx.subtract(BigInteger.ONE));
    }
}
