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

interface BitOperators {
    BitOperators combine(BitOperators operators);

    BitOperators bitOpsWith(IntegerBitOperators operators);

    BitOperators bitOpsWith(LongBitOperators operators);

    BitOperators bitOpsWith(BigIntegerBitOperators operators);

    public Number not(Number value);

    public Number and(Number x, Number y);

    public Number or(Number x, Number y);

    public Number xor(Number x, Number y);

    public Number andNot(Number x, Number y);

    public Number clearBit(Number value, int n);

    public Number setBit(Number value, int n);

    public Number flipBit(Number value, int n);

    public boolean testBit(Number value, int n);

    public Number shiftLeft(Number value, int n);

    public Number shiftRight(Number value, int n);
}
