package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.divide;
import static com.googlecode.totallylazy.numbers.Numbers.isZero;
import static com.googlecode.totallylazy.numbers.Numbers.multiply;

public class Average extends CombinerFunction<java.lang.Number> {
    public java.lang.Number call(java.lang.Number average, java.lang.Number value) {
        return averageNumber(average).combine(averageNumber(value));
    }

    @Override
    public java.lang.Number identity() {
        return averageNumber(0);
    }

    public static Average.Number averageNumber(java.lang.Number number, java.lang.Number count) {
        return new Average.Number(number, count);
    }

    public static Average.Number averageNumber(java.lang.Number number) {
        if(number instanceof Number) return (Number) number;
        return averageNumber(number, isZero(number) ? 0 : 1);
    }

    private static class Number extends DelegatingNumber {
        private final java.lang.Number count;

        private Number(java.lang.Number number, java.lang.Number count) {
            super(number);
            this.count = count;
        }

        public Number combine(Number value) {
            java.lang.Number newCount = add(count, value.count);
            return averageNumber(divide(add(sum(), value.sum()), newCount), newCount);
        }

        private java.lang.Number sum() {
            return multiply(count, value());
        }
    }
}
