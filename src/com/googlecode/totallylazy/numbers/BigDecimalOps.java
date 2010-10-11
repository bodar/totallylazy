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

import java.math.BigDecimal;
import java.math.MathContext;

final class BigDecimalOps implements Ops {
    final static MathContext MATH_CONTEXT = MathContext.UNLIMITED;

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
        return this;
    }

    public boolean isZero(Number x) {
        BigDecimal bx = (BigDecimal) x;
        return bx.signum() == 0;
    }

    public boolean isPos(Number x) {
        BigDecimal bx = (BigDecimal) x;
        return bx.signum() > 0;
    }

    public boolean isNeg(Number x) {
        BigDecimal bx = (BigDecimal) x;
        return bx.signum() < 0;
    }

    final public Number add(Number x, Number y) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? Numbers.toBigDecimal(x).add(Numbers.toBigDecimal(y))
                : Numbers.toBigDecimal(x).add(Numbers.toBigDecimal(y), mc);
    }

    final public Number multiply(Number x, Number y) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? Numbers.toBigDecimal(x).multiply(Numbers.toBigDecimal(y))
                : Numbers.toBigDecimal(x).multiply(Numbers.toBigDecimal(y), mc);
    }

    public Number divide(Number x, Number y) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? Numbers.toBigDecimal(x).divide(Numbers.toBigDecimal(y))
                : Numbers.toBigDecimal(x).divide(Numbers.toBigDecimal(y), mc);
    }

    public Number quotient(Number x, Number y) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? Numbers.toBigDecimal(x).divideToIntegralValue(Numbers.toBigDecimal(y))
                : Numbers.toBigDecimal(x).divideToIntegralValue(Numbers.toBigDecimal(y), mc);
    }

    public Number remainder(Number x, Number y) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? Numbers.toBigDecimal(x).remainder(Numbers.toBigDecimal(y))
                : Numbers.toBigDecimal(x).remainder(Numbers.toBigDecimal(y), mc);
    }

    public boolean equiv(Number x, Number y) {
        return Numbers.toBigDecimal(x).equals(Numbers.toBigDecimal(y));
    }

    public boolean lt(Number x, Number y) {
        return Numbers.toBigDecimal(x).compareTo(Numbers.toBigDecimal(y)) < 0;
    }

    //public Number subtract(Number x, Number y);
    final public Number negate(Number x) {
        MathContext mc = MATH_CONTEXT;
        return mc == null
                ? ((BigDecimal) x).negate()
                : ((BigDecimal) x).negate(mc);
    }

    public Number inc(Number x) {
        MathContext mc = (MathContext) MATH_CONTEXT;
        BigDecimal bx = (BigDecimal) x;
        return mc == null
                ? bx.add(BigDecimal.ONE)
                : bx.add(BigDecimal.ONE, mc);
    }

    public Number dec(Number x) {
        MathContext mc = (MathContext) MATH_CONTEXT;
        BigDecimal bx = (BigDecimal) x;
        return mc == null
                ? bx.subtract(BigDecimal.ONE)
                : bx.subtract(BigDecimal.ONE, mc);
    }
}
