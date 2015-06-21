package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Binary;
import com.googlecode.totallylazy.Function3;
import com.googlecode.totallylazy.Monoid;
import com.googlecode.totallylazy.Reducer;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.UnaryFunction;
import com.googlecode.totallylazy.numbers.Integers;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.PersistentMap.constructors.map;

public class SelectionTest {
    Keyword<String> name = Keyword.keyword();
    Keyword<Integer> age = Keyword.keyword();

    PersistentMap<String, Object> dan = map(name.name(), "Dan", age.name(), 21);
    PersistentMap<String, Object> matt = map(name.name(), "Matt", age.name(), 22);
    Sequence<PersistentMap<String, Object>> data = sequence(dan, matt);


    @Test
    public void canFilter() throws Exception {
        assertThat(data.filter(where(name, is("Dan"))), is(one(dan)));
    }

    @Test
    public void canSelect() throws Exception {
        assertThat(data.map(name), is(sequence("Dan", "Matt")));
        assertThat(data.map(select(name)), is(sequence(map("name", "Dan"), map("name", "Matt"))));
    }

    @Test
    public void canReduce() throws Exception {
        Aggregate<String, String> minimumName = Aggregate.aggregate(name, Strings.minimum);
        Aggregate<Integer, Integer> minimumAge = Aggregate.aggregate(age, Integers.minimum());
        assertThat(data.reduce(minimumAge), is(21));
        assertThat(data.reduce(minimumName), is("Dan"));
        assertThat(data.reduce(select(minimumName, minimumAge)), is(map("name", "Dan", "age", 21)));
    }

    @Test
    public void supportsConcatination() throws Exception {
        Keyword<String> concat = concat(name, age);
        assertThat(data.filter(where(name, is("Dan"))).map(concat), is(sequence("Dan21")));
//        assertThat(data.filter(where(name, is("Dan"))).map(select(concat)), is(sequence(map(concat.name(), "Dan21"))));
    }

    private Keyword<String> concat(Keyword<?>... functions) {
        return new Keyword<String>() {
            @Override
            public Class<String> forClass() {
                return String.class;
            }

            Sequence<Keyword<?>> keywords = sequence(functions);

            @Override
            public String name() {
                return keywords.map(Keyword::name).toString("concat(", ",", ")");
            }

            @Override
            public String call(Map<String, Object> map) throws Exception {
                return keywords.fold(new StringBuilder(), (b, k) -> b.append(k.apply(map))).toString();
            }
        };
    }

    private Projection<PersistentMap<String, Object>> select(Keyword<?>... selections) {
        return select((selection, source, destination) -> {
            Object value = selection.apply(source);
            return destination.insert(selection.name(), value);
        }, selections);
    }

    @SuppressWarnings("unchecked")
    private Projection<PersistentMap<String, Object>> select(Aggregate<?, ?>... selections) {
        return select((selection, source, destination) -> {
            Aggregate raw = selection;
            Object sourceValue = raw.keyword().apply(source);
            sourceValue = sourceValue == null ? raw.identity() : sourceValue;
            Object destinationValue = raw.apply(sourceValue, destination);
            return destination.insert(raw.keyword().name(), destinationValue);
        }, selections);
    }

    @SafeVarargs
    private final <T> Projection<PersistentMap<String, Object>> select(Function3<? super T, PersistentMap<String, Object>, PersistentMap<String, Object>, PersistentMap<String, Object>> fun,
                                                                 T... selections) {
        return Projection.projection(map(), (seed, row) ->
                sequence(selections).fold(seed, (accumulator, selection) -> fun.apply(selection, row, accumulator)));
    }

     interface Projection<T> extends Monoid<T>, UnaryFunction<T> {
        @Override
        default T call(T t) throws Exception {
            return call(identity(), t);
        }

        static <T> Projection<T> projection(T identity, Binary<T> binary){
            return new Projection<T>() {
                @Override
                public T call(T t, T t2) throws Exception {
                    return binary.call(t, t2);
                }

                @Override
                public T identity() {
                    return identity;
                }
            };
        }
    }
    interface Aggregate<T, R> extends Reducer<PersistentMap<String,Object>, R> {
        Keyword<T> keyword();

        Reducer<T, R> reducer();

        static <T, R> Aggregate<T, R> aggregate(Keyword<T> keyword, Reducer<T, R> reducer) {
            return new Aggregate<T, R>() {
                @Override
                public Keyword<T> keyword() {
                    return keyword;
                }

                @Override
                public Reducer<T, R> reducer() {
                    return reducer;
                }
            };
        }

        @Override
        default R call(R seed, PersistentMap<String, Object> data) throws Exception {
            return reducer().call(seed, keyword().call(data));
        }

        @Override
        default R identity() {
            return reducer().identity();
        }


    }
}
