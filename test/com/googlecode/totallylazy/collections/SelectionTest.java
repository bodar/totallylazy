package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Binary;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Monoid;
import com.googlecode.totallylazy.functions.Reducer;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Strings;
import com.googlecode.totallylazy.functions.Unary;
import com.googlecode.totallylazy.numbers.Integers;
import org.junit.Test;

import java.util.Map;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Sequences.one;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.PersistentMap.constructors.map;

public class SelectionTest {
    Keyword<String> name = Keyword.keyword();
    Keyword<Integer> age = Keyword.keyword();

    PersistentMap<String, Object> dan = map(name.name(), "Dan", age.name(), 21);
    PersistentMap<String, Object> matt = map(name.name(), "Matt", age.name(), 22);
    PersistentMap<String, Object> bob = map(name.name(), "Bob", age.name(), 22);
    Sequence<PersistentMap<String, Object>> data = sequence(dan, matt, bob);


    @Test
    public void canFilter() throws Exception {
        assertThat(data.filter(where(name, is("Dan"))), is(one(dan)));
    }

    @Test
    public void canSelect() throws Exception {
        assertThat(data.map(name), is(sequence("Dan", "Matt", "Bob")));
        assertThat(data.map(select(name)), is(sequence(map("name", "Dan"), map("name", "Matt"), map("name", "Bob"))));
    }

    @Test
    public void canReduce() throws Exception {
        Aggregate<String, String> minimumName = Aggregate.aggregate(name, Strings.minimum);
        Aggregate<Integer, Integer> minimumAge = Aggregate.aggregate(age, Integers.minimum());
        assertThat(data.reduce(minimumAge), is(21));
        assertThat(data.reduce(minimumName), is("Bob"));
        assertThat(data.reduce(select(minimumName, minimumAge)), is(map("name", "Bob", "age", 21)));
    }

    @Test
    public void supportsConcatination() throws Exception {
        Keyword<String> concat = composite(Concat.Instance, name, age);
        assertThat(data.filter(where(name, is("Dan"))).map(concat), is(sequence("Dan21")));
        assertThat(data.filter(where(name, is("Dan"))).map(select(concat)), is(one(map(concat.name(), "Dan21"))));
    }

    @Test
    public void supportsGroupByAndConcatication() throws Exception {
        Aggregate<String, String> join = Aggregate.aggregate(name, Concat.Instance);
        assertThat(data.groupBy(age).map(group -> group.reduce(join)), is(sequence("Dan", "MattBob")));
    }

    @Test
    public void supportsUppercase() throws Exception {
        Keyword<String> upperCase = compose(name, String::toUpperCase);
        assertThat(data.filter(where(name, is("Dan"))).map(upperCase), is(sequence("DAN")));
        assertThat(data.filter(where(name, is("Dan"))).map(select(upperCase)), is(one(map(upperCase.name(), "DAN"))));
    }

    private <T, R> Keyword<R> compose(Keyword<T> keyword, Function1<T, R> function) {
        return new Keyword<R>() {
            @Override
            public String name() {
                return function.toString() + "(" + keyword.name() + ")";
            }

            @Override
            public Class<R> forClass() {
                return null;
            }

            @Override
            public R call(Map<String, Object> map) throws Exception {
                return function.call(keyword.call(map));
            }
        };
    }

    @SafeVarargs
    public final <T, R> Keyword<R> composite(Reducer<? super T, R> reducer, Keyword<? extends T>... functions) {
        return new Keyword<R>() {
            Sequence<Keyword<? extends T>> keywords = sequence(functions);

            @Override
            public Class<R> forClass() {
                return null;
            }

            @Override
            public String name() {
                return keywords.map(Keyword::name).toString(reducer.toString() + "(", ",", ")");
            }

            @Override
            public R call(Map<String, Object> map) throws Exception {
                return keywords.fold(reducer.identity(), (a, k) -> reducer.call(a, k.apply(map)));
            }
        };
    }

    private Projection<PersistentMap<String, Object>> select(Selection... selections) {
        return Projection.projection(map(), (seed, row) ->
                sequence(selections).fold(seed, (accumulator, selection) -> selection.select(row, accumulator)));
    }

    interface Projection<T> extends Monoid<T>, Unary<T> {
        @Override
        default T call(T t) throws Exception {
            return call(identity(), t);
        }

        static <T> Projection<T> projection(T identity, Binary<T> binary) {
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

    enum Concat implements Reducer<Object, String> {
        Instance;

        @Override
        public String call(String s, Object o) throws Exception {
            return s + o;
        }

        @Override
        public String identity() {
            return "";
        }
    }

    interface Aggregate<T, R> extends Reducer<PersistentMap<String, Object>, R>, Selection {
        Keyword<? extends T> keyword();

        Reducer<? super T, R> reducer();

        static <T, R> Aggregate<T, R> aggregate(Keyword<? extends T> keyword, Reducer<? super T, R> reducer) {
            return new Aggregate<T, R>() {
                @Override
                public Keyword<? extends T> keyword() {
                    return keyword;
                }

                @Override
                public Reducer<? super T, R> reducer() {
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

        @Override
        default PersistentMap<String, Object> select(PersistentMap<String, Object> source, PersistentMap<String, Object> destination) {
            T sourceValue = keyword().apply(source);
            R value = sourceValue == null ? identity() : cast(sourceValue);
            R reduced = apply(value, destination);
            return destination.insert(keyword().name(), reduced);
        }
    }
}
