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

final class BigIntegerBitOps implements BitOps {
    public BitOps combine(BitOps y) {
        return y.bitOpsWith(this);
    }

    final public BitOps bitOpsWith(IntegerBitOps x) {
        return this;
    }

    final public BitOps bitOpsWith(LongBitOps x) {
        return this;
    }

    final public BitOps bitOpsWith(BigIntegerBitOps x) {
        return this;
    }

    public Number not(Number x) {
        return Numbers.toBigInteger(x).not();
    }

    public Number and(Number x, Number y) {
        return Numbers.toBigInteger(x).and(Numbers.toBigInteger(y));
    }

    public Number or(Number x, Number y) {
        return Numbers.toBigInteger(x).or(Numbers.toBigInteger(y));
    }

    public Number xor(Number x, Number y) {
        return Numbers.toBigInteger(x).xor(Numbers.toBigInteger(y));
    }

    public Number andNot(Number x, Number y) {
        return Numbers.toBigInteger(x).andNot(Numbers.toBigInteger(y));
    }

    public Number clearBit(Number x, int n) {
        return Numbers.toBigInteger(x).clearBit(n);
    }

    public Number setBit(Number x, int n) {
        return Numbers.toBigInteger(x).setBit(n);
    }

    public Number flipBit(Number x, int n) {
        return Numbers.toBigInteger(x).flipBit(n);
    }

    public boolean testBit(Number x, int n) {
        return Numbers.toBigInteger(x).testBit(n);
    }

    public Number shiftLeft(Number x, int n) {
        return Numbers.toBigInteger(x).shiftLeft(n);
    }

    public Number shiftRight(Number x, int n) {
        return Numbers.toBigInteger(x).shiftRight(n);
    }
}
