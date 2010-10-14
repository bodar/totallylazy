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

package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable2;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.googlecode.totallylazy.Callables.toClass;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Numbers {
    public static <T extends Number> Number negate(T x) {
        return operatorsFor(x).negate(x);
    }

    public static <T extends Number> Number increment(T x) {
        return operatorsFor(x).increment(x);
    }

    public static <T extends Number> Number decrement(T x) {
        return operatorsFor(x).decrement(x);
    }

    public static <T extends Number> boolean isZero(T x) {
        return operatorsFor(x).isZero(x);
    }

    public static <T extends Number> boolean isPositive(T x) {
        return operatorsFor(x).isPositive(x);
    }

    public static <T extends Number> boolean isNegative(T x) {
        return operatorsFor(x).isNegative(x);
    }

    public static boolean equalTo(Number x, Number y) {
        return operatorsFor(x, y).equalTo(x, y);
    }

    public static boolean lessThan(Number x, Number y) {
        return operatorsFor(x, y).lessThan(x, y);
    }

    public static boolean lessThanOrEqual(Number x, Number y) {
        return !operatorsFor(x, y).lessThan(y, x);
    }

    public static boolean greaterThan(Number x, Number y) {
        return operatorsFor(x, y).lessThan(y, x);
    }

    public static boolean greaterThanOrEqual(Number x, Number y) {
        return !operatorsFor(x,y).lessThan(x, y);
    }

    public static int compare(Number x, Number y) {
        Operators operators = operatorsFor(x,y);
        if (operators.lessThan(x, y))
            return -1;
        else if (operators.lessThan(y, x))
            return 1;
        return 0;
    }

    public static Comparator<Number> ascending(){
        return new Comparator<Number>() {
            public int compare(Number x, Number y) {
                return Numbers.compare(x,y);
            }
        };
    }

    public static Comparator<Number> descending(){
        return new Comparator<Number>() {
            public int compare(Number x, Number y) {
                return ~Numbers.compare(x,y);
            }
        };
    }

    public static <X extends Number, Y extends Number> Number add(X x, Y y) {
        return operatorsFor(x,y).add(x, y);
    }

    public static <X extends Number, Y extends Number>  Number subtract(X x, Y y) {
        return operatorsFor(x, y).add(x, operatorsFor(y).negate(y));
    }

    public static <X extends Number, Y extends Number>  Number multiply(X x, Y y) {
        return operatorsFor(x, y).multiply(x, y);
    }

    public static <X extends Number, Y extends Number> Number divide(X x, Y y) {
        throwIfZero(y);
        return operatorsFor(x, y).divide(x, y);
    }

    private static <T extends Number> void throwIfZero(T value) {
        if (operatorsFor(value).isZero(value)){
            throw new ArithmeticException("Divide by zero");
        }
    }

    public static <X extends Number, Y extends Number> Number quotient(X x, Y y) {
        throwIfZero(y);
        return reduce(operatorsFor(x, y).quotient(x, y));
    }

    public static <X extends Number, Y extends Number> Number remainder(X x, Y y) {
        throwIfZero(y);
        return reduce(operatorsFor(x, y).remainder(x, y));
    }

    public static Number rationalize(Number x) {
        if (x instanceof Float || x instanceof Double)
            return rationalize(BigDecimal.valueOf(x.doubleValue()));
        else if (x instanceof BigDecimal) {
            BigDecimal bx = (BigDecimal) x;
            BigInteger bv = bx.unscaledValue();
            int scale = bx.scale();
            if (scale < 0)
                return bv.multiply(BigInteger.TEN.pow(-scale));
            else
                return divide(bv, BigInteger.TEN.pow(scale));
        }
        return x;
    }

    public static Number reduce(Number value) {
        if (value instanceof Long)
            return LongOperators.reduce(value.longValue());
        else if (value instanceof BigInteger)
            return BigIntegerOperators.reduce((BigInteger) value);
        return value;
    }

    public static Number not(Number x) {
        return bitOps(x).not(x);
    }

    public static Number and(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).and(x, y);
    }

    public static Number or(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).or(x, y);
    }

    public static Number xor(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).xor(x, y);
    }

    public static Number andNot(Number x, Number y) {
        return bitOps(x).combine(bitOps(y)).andNot(x, y);
    }

    public static Number clearBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).clearBit(x, n);
    }

    public static Number setBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).setBit(x, n);
    }

    public static Number flipBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).flipBit(x, n);
    }

    public static boolean testBit(Number x, int n) {
        if (n < 0)
            throw new ArithmeticException("Negative bit index");
        return bitOps(x).testBit(x, n);
    }

    public static Number shiftLeft(Number x, Number n) {
        return bitOps(x).shiftLeft(x, (n).intValue());
    }

    public static int shiftLeft(int x, int n) {
        return x << n;
    }

    public static Number shiftRight(Number x, Number n) {
        return bitOps(x).shiftRight(x, (n).intValue());
    }

    public static int shiftRight(int x, int n) {
        return x >> n;
    }

    static final IntegerBitOperators INTEGER_BITOPS = new IntegerBitOperators();
    static final LongBitOperators LONG_BITOPS = new LongBitOperators();
    static final BigIntegerBitOperators BIGINTEGER_BITOPS = new BigIntegerBitOperators();

    static final List<Operators> operators = new ArrayList<Operators>();
    static final List<Class> order = new ArrayList<Class>();

    static {
        addOperators(new IntegerOperators());
        addOperators(new LongOperators());
        addOperators(new BigIntegerOperators());
        addOperators(new BigDecimalOperators());
        addOperators(new RatioOperators());
        addOperators(new FloatOperators());
        addOperators(new DoubleOperators());
    }

    private static void addOperators(Operators newOperators) {
        operators.add(newOperators);
        order.add(newOperators.forClass());
    }

    public static <T extends Number> Operators<T> operatorsFor(Class<T> numberClass) {
        return (Operators<T>) operators.get(order.indexOf(numberClass));
    }

    public static <T extends Number> Operators<T> operatorsFor(T... numbers) {
        int highestIndex = 0;
        for (T number : numbers) {
            final int index = order.indexOf(number.getClass());
            if(index > highestIndex){
                highestIndex = index;
            }
        }

        return (Operators<T>) operators.get(highestIndex);
    }

    static BitOperators bitOps(Number x) {
        Class xc = x.getClass();

        if (xc == Integer.class)
            return INTEGER_BITOPS;
        else if (xc == Long.class)
            return LONG_BITOPS;
        else if (xc == BigInteger.class)
            return BIGINTEGER_BITOPS;
        else if (xc == Double.class || xc == Float.class || xc == BigDecimalOperators.class || xc == Ratio.class)
            throw new ArithmeticException("bit operation on non integer type: " + xc);
        else
            return INTEGER_BITOPS;
    }

}
