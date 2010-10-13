package com.googlecode.totallylazy.numbers;

import static com.googlecode.totallylazy.numbers.BigIntegerOperators.toBigInteger;

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

public final class BigIntegerBitOperators implements BitOperators {
    public final BitOperators combine(BitOperators operators) {
        return operators.bitOpsWith(this);
    }

    public final BitOperators bitOpsWith(IntegerBitOperators operators) {
        return this;
    }

    public final BitOperators bitOpsWith(LongBitOperators operators) {
        return this;
    }

    public final BitOperators bitOpsWith(BigIntegerBitOperators operators) {
        return this;
    }

    public final Number not(Number value) {
        return toBigInteger(value).not();
    }

    public final Number and(Number x, Number y) {
        return toBigInteger(x).and(toBigInteger(y));
    }

    public final Number or(Number x, Number y) {
        return toBigInteger(x).or(toBigInteger(y));
    }

    public final Number xor(Number x, Number y) {
        return toBigInteger(x).xor(toBigInteger(y));
    }

    public final Number andNot(Number x, Number y) {
        return toBigInteger(x).andNot(toBigInteger(y));
    }

    public final Number clearBit(Number value, int n) {
        return toBigInteger(value).clearBit(n);
    }

    public final Number setBit(Number value, int n) {
        return toBigInteger(value).setBit(n);
    }

    public final Number flipBit(Number value, int n) {
        return toBigInteger(value).flipBit(n);
    }

    public final boolean testBit(Number value, int n) {
        return toBigInteger(value).testBit(n);
    }

    public final Number shiftLeft(Number value, int n) {
        return toBigInteger(value).shiftLeft(n);
    }

    public final Number shiftRight(Number value, int n) {
        return toBigInteger(value).shiftRight(n);
    }
}
