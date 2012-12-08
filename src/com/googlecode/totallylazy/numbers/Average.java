package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.CombinerFunction;

import static com.googlecode.totallylazy.numbers.Numbers.isZero;

public class Average extends CombinerFunction<java.lang.Number> {
    public java.lang.Number call(java.lang.Number average, java.lang.Number value) {
        return weighted(average).combine(weighted(value));
    }

    @Override
    public java.lang.Number identity() {
        return weighted(0);
    }

    public static Weighted weighted(java.lang.Number number, java.lang.Number count) {
        return new Weighted(number, count);
    }

    public static Weighted weighted(java.lang.Number number) {
        if (number instanceof Weighted) return (Weighted) number;
        return weighted(number, isZero(number) ? 0 : 1);
    }

    private static class Weighted extends Num {
        private final Num weight;

        private Weighted(java.lang.Number value, java.lang.Number weight) {
            super(value);
            this.weight = num(weight);
        }

        public Weighted combine(Weighted value) {
            Num newCount = weight.add(value.weight);
            return weighted(sum().add(value.sum()).divide(newCount), newCount);
        }

        private Num sum() {
            return weight.multiply(value());
        }
    }
}
