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

interface Ops {
    Ops combine(Ops y);

    Ops opsWith(IntegerOps x);

    Ops opsWith(LongOps x);

    Ops opsWith(FloatOps x);

    Ops opsWith(DoubleOps x);

    Ops opsWith(RatioOps x);

    Ops opsWith(BigIntegerOps x);

    Ops opsWith(BigDecimalOps x);

    public boolean isZero(Number x);

    public boolean isPositive(Number x);

    public boolean isNegative(Number x);

    public Number add(Number x, Number y);

    public Number multiply(Number x, Number y);

    public Number divide(Number x, Number y);

    public Number quotient(Number x, Number y);

    public Number remainder(Number x, Number y);

    public boolean equalTo(Number x, Number y);

    public boolean lessThan(Number x, Number y);

    public Number negate(Number x);

    public Number increment(Number x);

    public Number decrement(Number x);
}
