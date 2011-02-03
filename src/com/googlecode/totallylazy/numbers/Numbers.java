/*
This code is a a heavily modified version of Numbers from Rich Hickeys clojure core
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


package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Callable2;
import com.googlecode.totallylazy.Callables;
import com.googlecode.totallylazy.MemorisedSequence;
import com.googlecode.totallylazy.Predicate;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.predicates.RemainderIs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.reduceAndShift;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Numbers {
    public static Number valueOf(String string) {
        return reduce(new BigInteger(string));
    }

    public static Sequence<Number> numbers(Number... numbers) {
        return Sequences.sequence(numbers);
    }

    public static Sequence<Number> numbers(int[] numbers) {
        return Sequences.sequence(new IntIterator(numbers));
    }

    // TODO: Try to convert to lazy sequence again!
    public static Sequence<Number> primeFactorsOf(Number value) {
        Set<Number> factors = new HashSet<Number>();
        Number ceiling = value;
        Number possibleFactor = 2;

        while (lessThanOrEqualTo(squared(possibleFactor), ceiling)) {
            if (isZero(remainder(ceiling, possibleFactor))) {
                factors.add(possibleFactor);
                ceiling = divide(ceiling, possibleFactor);
            } else {
                possibleFactor = increment(possibleFactor);
            }
        }
        if (!equalTo(ceiling, 1)) {
            factors.add(ceiling);
        }
        return sequence(factors).sortBy(ascending());
    }

    private static Number squared(Number value) {
        return multiply(value, value);
    }

    public static LogicalPredicate<Number> even() {
        return remainderIs(2, 0);
    }

    public static LogicalPredicate<Number> odd() {
        return remainderIs(2, 1);
    }

    public static LogicalPredicate<Number> prime() {
        return new LogicalPredicate<Number>() {
            public final boolean matches(final Number candidate) {
                return primes().takeWhile(primeSquaredLessThan(candidate)).forAll(not(remainderIsZero(candidate)));
            }
        };
    }

    public static LogicalPredicate<Number> primeSquaredLessThan(final Number candidate) {
        return new LogicalPredicate<Number>() {
            public final boolean matches(final Number prime) {
                return Numbers.lessThanOrEqualTo(squared(prime), candidate);
            }
        };
    }

    public static LogicalPredicate<Number> remainderIsZero(final Number dividend) {
        return new LogicalPredicate<Number>() {
            public final boolean matches(Number divisor) {
                return Numbers.isZero(remainder(dividend, divisor));
            }
        };
    }

    public static LogicalPredicate<Number> remainderIs(final Number divisor, final Number remainder) {
        return new RemainderIs(divisor, remainder);
    }

    private static final MemorisedSequence<Number> primes = Sequences.<Number>sequence(2).join(iterate(Numbers.add(2), 3).filter(prime())).memorise();

    public static MemorisedSequence<Number> primes() {
        return primes;
    }

    public static Sequence<Number> fibonacci() {
        return iterate(reduceAndShift(add()), Sequences.<Number>sequence(0, 1)).map(Callables.<Number>first());
    }

    public static Sequence<Number> powersOf(Number amount) {
        return iterate(multiply(amount), 1);
    }

    public static <T> Operators operatorsFor(Class<T> numberClass) {
        if (numberClass == Integer.class) return IntegerOperators.Instance;
        if (numberClass == Long.class) return LongOperators.Instance;
        if (numberClass == BigInteger.class) return BigIntegerOperators.Instance;
        if (numberClass == BigDecimal.class) return BigDecimalOperators.Instance;
        if (numberClass == Ratio.class) return RatioOperators.Instance;
        if (numberClass == Float.class) return FloatOperators.Instance;
        if (numberClass == Double.class) return DoubleOperators.Instance;
        throw new UnsupportedOperationException("Unsupported number class " + numberClass);
    }

    public static <T extends Number> Operators<T> operatorsFor(T number) {
        return (Operators<T>) operatorsFor(number.getClass());
    }

    public static <T extends Number> Operators<T> operatorsFor(T a, T b) {
        Operators aOperators = operatorsFor(a.getClass());
        Operators bOperators = operatorsFor(b.getClass());

        return aOperators.priority() > bOperators.priority() ? aOperators : bOperators;
    }

    public static <T extends Number> Number negate(T value) {
        return operatorsFor(value).negate(value);
    }

    public static Callable1<Number, Number> increment() {
        return add(1);
    }

    public static <T extends Number> Number increment(T value) {
        return operatorsFor(value).increment(value);
    }

    public static <T extends Number> Number decrement(T value) {
        return operatorsFor(value).decrement(value);
    }

    public static <T extends Number> boolean isZero(T value) {
        return operatorsFor(value).isZero(value);
    }

    public static <T extends Number> boolean isPositive(T value) {
        return operatorsFor(value).isPositive(value);
    }

    public static <T extends Number> boolean isNegative(T value) {
        return operatorsFor(value).isNegative(value);
    }

    public static boolean equalTo(Number x, Number y) {
        return operatorsFor(x, y).equalTo(x, y);
    }

    public static Predicate<Number> lessThan(final Number value) {
        return new LessThanPredicate(value);
    }

    public static boolean lessThan(Number x, Number y) {
        return operatorsFor(x, y).lessThan(x, y);
    }

    public static LogicalPredicate<Number> lessThanOrEqualTo(final Number value) {
        return new LessThanOrEqualToPredicate(value);
    }

    public static boolean lessThanOrEqualTo(Number x, Number y) {
        return !operatorsFor(x, y).lessThan(y, x);
    }

    public static LogicalPredicate<Number> greaterThan(final Number value) {
        return new GreaterThanPredicate(value);
    }

    public static boolean greaterThan(Number x, Number y) {
        return operatorsFor(x, y).lessThan(y, x);
    }

    public static LogicalPredicate<Number> greaterThanOrEqualTo(final Number value) {
        return new GreaterThanOrEqualToPredicate(value);
    }

    public static boolean greaterThanOrEqualTo(Number x, Number y) {
        return !operatorsFor(x, y).lessThan(x, y);
    }

    public static LogicalPredicate<Number> between(final Number a, final Number b) {
        return new BetweenPredicate(a, b);
    }

    public static int compare(Number x, Number y) {
        Operators operators = operatorsFor(x, y);
        if (operators.lessThan(x, y))
            return -1;
        else if (operators.lessThan(y, x))
            return 1;
        return 0;
    }

    public static Comparator<Number> ascending() {
        return new Comparator<Number>() {
            public int compare(Number x, Number y) {
                return Numbers.compare(x, y);
            }
        };
    }

    public static Comparator<Number> descending() {
        return new Comparator<Number>() {
            public int compare(Number x, Number y) {
                return Numbers.compare(y, x);
            }
        };
    }

    public static Callable1<Iterable<Number>, Number> sum() {
        return new Callable1<Iterable<Number>, Number>() {
            public Number call(Iterable<Number> numbers) throws Exception {
                return Sequences.reduceLeft(numbers, add());
            }
        };
    }

    public static <T extends Number> Callable2<T, T, Number> add() {
        return new Callable2<T, T, Number>() {
            public Number call(T a, T b) {
                return Numbers.add(a, b);
            }
        };
    }

    public static Callable1<Number, Number> add(final Number amount) {
        return curry(add(), amount);
    }

    public static <X extends Number, Y extends Number> Number add(X x, Y y) {
        return operatorsFor(x, y).add(x, y);
    }

    public static <X extends Number, Y extends Number> Number subtract(X x, Y y) {
        return operatorsFor(x, y).add(x, operatorsFor(y).negate(y));
    }

    public static <T extends Number> Callable2<T, T, Number> multiply() {
        return new Callable2<T, T, Number>() {
            public Number call(T multiplicand, T multiplier) throws Exception {
                return Numbers.multiply(multiplicand, multiplier);
            }
        };
    }

    public static <T extends Number> Callable1<T, Number> multiply(final T multiplicand) {
        return curry(Numbers.<T>multiply(), multiplicand);
    }

    public static <X extends Number, Y extends Number> Number multiply(X x, Y y) {
        return operatorsFor(x, y).multiply(x, y);
    }

    public static <X extends Number, Y extends Number> Number divide(X x, Y y) {
        throwIfZero(y);
        return operatorsFor(x, y).divide(x, y);
    }

    public static <X extends Number, Y extends Number> Number quotient(X x, Y y) {
        throwIfZero(y);
        return reduce(operatorsFor(x, y).quotient(x, y));
    }

    public static <X extends Number, Y extends Number> Number remainder(X x, Y y) {
        throwIfZero(y);
        return reduce(operatorsFor(x, y).remainder(x, y));
    }

    private static <T extends Number> void throwIfZero(T value) {
        if (operatorsFor(value).isZero(value)) {
            throw new ArithmeticException("Divide by zero");
        }
    }

    public static Number reduce(Number value) {
        if (value instanceof Long)
            return LongOperators.reduce(value.longValue());
        else if (value instanceof BigInteger)
            return BigIntegerOperators.reduce((BigInteger) value);
        return value;
    }

    public static Callable1<Number, Character> toCharacter() {
        return new Callable1<Number, Character>() {
            public Character call(Number number) throws Exception {
                return (char) number.shortValue();
            }
        };
    }
}
