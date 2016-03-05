package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface GroupByTransducer<T, K> extends Transducer<T, Group<K, T>> {
    Function<? super T, ? extends K> keyExtractor();

    static <T, K> GroupByTransducer<T, K> groupByTransducer(Function<? super T, ? extends K> keyExtractor) {return () -> keyExtractor;}

    @Override
    default Observer<T> apply(Observer<Group<K, T>> observer) {
        return new Observer<T>() {
            private final Map<K, Group<K, T>> groups = new ConcurrentHashMap<>();

            @Override
            public State start() {
                observers().each(Observer::start);
                return observer.start();
            }

            @Override
            public State next(T item) {
                groups.computeIfAbsent(keyExtractor().apply(item), k -> {
                    Group<K, T> group = new Group<>(k);
                    observer.next(group);
                    return group;
                }).next(item);
                return State.Continue;
            }

            @Override
            public void error(Throwable throwable) {
                observers().each(o -> o.error(throwable));
                observer.error(throwable);
            }

            @Override
            public void finish() {
                observers().each(Observer::finish);
                observer.finish();
            }

            private Sequence<Observer<T>> observers() {
                return Sequences.sequence(groups.values());
            }
        };
    }
}
