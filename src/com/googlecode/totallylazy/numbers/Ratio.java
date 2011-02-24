package com.googlecode.totallylazy.numbers;

/*
This code is a a modified version of Ratio from Rich Hickeys clojure core
 */

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
import java.math.BigInteger;
import java.math.MathContext;

public final class Ratio extends Number implements Comparable {
    final public BigInteger numerator;
    final public BigInteger denominator;

    public Ratio(BigInteger numerator, BigInteger denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public final boolean equals(Object arg0) {
        return arg0 != null
                && arg0 instanceof Ratio
                && ((Ratio) arg0).numerator.equals(numerator)
                && ((Ratio) arg0).denominator.equals(denominator);
    }

    public final int hashCode() {
        return numerator.hashCode() ^ denominator.hashCode();
    }

    public final String toString() {
        return numerator.toString() + "/" + denominator.toString();
    }

    public final int intValue() {
        return (int) doubleValue();
    }

    public final long longValue() {
        return bigIntegerValue().longValue();
    }

    public final float floatValue() {
        return (float) doubleValue();
    }

    public final double doubleValue() {
        return decimalValue(MathContext.DECIMAL64).doubleValue();
    }

    public final BigDecimal decimalValue() {
        return decimalValue(MathContext.UNLIMITED);
    }

    public final BigDecimal decimalValue(MathContext mc) {
        BigDecimal numerator = new BigDecimal(this.numerator);
        BigDecimal denominator = new BigDecimal(this.denominator);

        return numerator.divide(denominator, mc);
    }

    public final BigInteger bigIntegerValue() {
        return numerator.divide(denominator);
    }

    public final int compareTo(Object o) {
        Number other = (Number) o;
        return Numbers.compare(this, other);
    }
}