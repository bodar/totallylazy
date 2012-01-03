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

import com.googlecode.totallylazy.*;
import com.googlecode.totallylazy.predicates.LogicalPredicate;
import com.googlecode.totallylazy.predicates.RemainderIs;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.googlecode.totallylazy.Callables.curry;
import static com.googlecode.totallylazy.Callables.reduceAndShift;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.characters;
import static com.googlecode.totallylazy.Sequences.iterate;
import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.Sequences.sequence;

public class Numbers {
    public static Sequence<Number> range(final Number start) {
        return iterate(increment(), start);
    }

    public static Sequence<Number> range(final Number start, final Number end) {
        return range(start).takeWhile(lessThan(end));
    }

    public static Sequence<Number> range(final Number start, final Number end, final Number step) {
        return iterate(add(step), start).takeWhile(lessThan(end));
    }

    public static Option<Number> valueOf(String string) {
        try {
            return some(reduce(new BigDecimal(string)));
        } catch(Exception e) {
            return none(Number.class);
        }
    }

    public static Sequence<Number> numbers(Number... numbers) {
        return Sequences.sequence(numbers);
    }

    public static Sequence<Number> numbers(final int[] numbers) {
        return new Sequence<Number>() {
            public Iterator<Number> iterator() {
                return new IntIterator(numbers);
            }
        };
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

    public static LogicalPredicate<Number> not(Number value) {
        return Predicates.not(value);
    }

    public static LogicalPredicate<Number> not(Predicate<? super Number> predicate) {
        return Predicates.not(predicate);
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
                return primes().takeWhile(primeSquaredLessThan(candidate)).forAll(Predicates.not(remainderIsZero(candidate)));
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

    private static final MemorisedSequence<Number> primes = Sequences.<Number>sequence(2).join(iterate(add(2), 3).filter(prime())).memorise();

    public static MemorisedSequence<Number> primes() {
        return primes;
    }

    public static Sequence<Number> fibonacci() {
        return iterate(reduceAndShift(sum()), numbers(0, 1)).map(Callables.first(Number.class));
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

    @SuppressWarnings("unchecked")
    public static <T extends Number> Operators<T> operatorsFor(T number) {
        return (Operators<T>) operatorsFor(number.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Number> Operators<T> operatorsFor(T a, T b) {
        Operators aOperators = operatorsFor(a.getClass());
        Operators bOperators = operatorsFor(b.getClass());

        return aOperators.priority() > bOperators.priority() ? aOperators : bOperators;
    }

    public static <T extends Number> Number negate(T value) {
        return operatorsFor(value).negate(value);
    }

    public static Function1<Number, Number> increment() {
        return new Function1<Number, Number>() {
            public Number call(Number number) throws Exception {
                return Numbers.increment(number);
            }
        };
    }

    public static <T extends Number> Number increment(T value) {
        return operatorsFor(value).increment(value);
    }

    public static Function1<Number, Number> decrement() {
        return new Function1<Number, Number>() {
            public Number call(Number number) throws Exception {
                return Numbers.decrement(number);
            }
        };
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

    public static LogicalPredicate<Number> lessThan(final Number value) {
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
        Operators<Number> operators = operatorsFor(x, y);
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

    public static Callable1<Iterable<Number>, Number> sumIterable() {
        return new Callable1<Iterable<Number>, Number>() {
            public Number call(Iterable<Number> numbers) throws Exception {
                return Sequences.reduceLeft(numbers, sum());
            }
        };
    }

    public static <T extends Number> Callable2<T, T, Number> average() {
        return new Average<T>();
    }

    public static <T extends Number> Function2<T, T, Number> sum() {
        return new Sum<T>();
    }

    public static <T extends Number> Function2<T, T, Number> add() {
        return new Sum<T>();
    }

    public static Callable1<Number, Number> add(final Number amount) {
        return add().apply(amount);
    }

    public static <X extends Number, Y extends Number> Number add(X x, Y y) {
        return operatorsFor(x, y).add(x, y);
    }

    public static <T extends Number> Callable2<T, T, Number> subtract() {
        return new Callable2<T, T, Number>() {
            public Number call(T a, T b) {
                return Numbers.subtract(a, b);
            }
        };
    }

    public static Callable1<Number, Number> subtract(final Number amount) {
        return new Callable1<Number, Number>() {
            public Number call(Number number) throws Exception {
                return Numbers.subtract(number, amount);
            }
        };
    }

    public static <X extends Number, Y extends Number> Number subtract(X x, Y y) {
        return operatorsFor(x, y).add(x, operatorsFor(y).negate(y));
    }

    public static <T extends Number> Function2<T, T, Number> product() {
        return multiply();
    }

    public static <T extends Number> Function2<T, T, Number> multiply() {
        return new Function2<T, T, Number>() {
            public Number call(T multiplicand, T multiplier) throws Exception {
                return multiply(multiplicand, multiplier);
            }
        };
    }

    public static <T extends Number> Function1<T, Number> multiply(final T multiplicand) {
        return Numbers.<T>multiply().apply(multiplicand);
    }

    public static <X extends Number, Y extends Number> Number multiply(X x, Y y) {
        return operatorsFor(x, y).multiply(x, y);
    }

    public static <X extends Number, Y extends Number> Number divide(X x, Y y) {
        throwIfZero(y);
        return operatorsFor(x, y).divide(x, y);
    }

    public static <X extends Number> Function1<Number, Number> divide(final X divisor) {
        return divide().flip().apply(divisor);
    }

    public static <T extends Number> Function2<T, T, Number> divide() {
        return new Function2<T, T, Number>() {
            public Number call(T dividend, T divisor) throws Exception {
                return divide(dividend, divisor);
            }
        };
    }

    public static <X extends Number, Y extends Number> Number quotient(X x, Y y) {
        throwIfZero(y);
        return reduce(operatorsFor(x, y).quotient(x, y));
    }

    public static <X extends Number, Y extends Number> Number remainder(X dividend, Y divisor) {
        throwIfZero(divisor);
        return reduce(operatorsFor(dividend, divisor).remainder(dividend, divisor));
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

    public static Callable1<Number, Number> remainder(final Number divisor) {
        return new Callable1<Number, Number>() {
            public Number call(Number dividend) throws Exception {
                return remainder(dividend, divisor);
            }
        };
    }

    public static String toLexicalString(Number value, final Number minValue, final Number maxValue) {
        String offset = add(value, negate(minValue)).toString();
        int maxSize = add(maxValue, negate(minValue)).toString().length();
        return repeat('0').take(maxSize - offset.length()).join(characters(offset)).toString("", "", "", Integer.MAX_VALUE);
    }

    public static Number parseLexicalString(String value, final Number minValue) {
        return add(valueOf(value).get(), minValue);
    }
}
