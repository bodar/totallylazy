package com.googlecode.totallylazy.numbers;

import com.googlecode.totallylazy.Combiner;

public class Average implements Combiner<Number> {
    public Number call(Number average, Number value) {
        return weighted(average).combine(weighted(value));
    }

    @Override
    public Number identityElement() {
        return weighted(0, 0);
    }

    public static Weighted weighted(Number number, Number count) {
        return new Weighted(number, count);
    }

    public static Weighted weighted(Number number) {
        if (number instanceof Weighted) return (Weighted) number;
        return weighted(number, 1);
    }

    private static class Weighted extends Num {
        private final Num weight;

        private Weighted(Number value, Number weight) {
            super(value);
            this.weight = num(weight);
        }

        public Weighted combine(Weighted value) {
            Num newWeight = weight.add(value.weight);
            return weighted(sum().add(value.sum()).divide(newWeight), newWeight);
        }

        private Num sum() {
            return weight.multiply(value());
        }
    }
}
