package com.googlecode.totallylazy.transducers;

import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.functions.Function1;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface GroupByTransducer<T, K> extends Transducer<T, Group<K, T>> {
    Function1<? super T, ? extends K> keyExtractor();

    static <T, K> GroupByTransducer<T, K> groupByTransducer(Function1<? super T, ? extends K> keyExtractor) {return () -> keyExtractor;}

    @Override
    default Receiver<T> apply(Receiver<Group<K, T>> receiver) {
        return new Receiver<T>() {
            private final Map<K, Group<K, T>> groups = new ConcurrentHashMap<>();

            @Override
            public State start() {
                receivers().each(Receiver::start);
                return receiver.start();
            }

            @Override
            public State next(T item) {
                groups.computeIfAbsent(keyExtractor().apply(item), k -> {
                    Group<K, T> group = new Group<>(k);
                    receiver.next(group);
                    return group;
                }).next(item);
                return State.Continue;
            }

            @Override
            public void error(Throwable throwable) {
                receivers().each(o -> o.error(throwable));
                receiver.error(throwable);
            }

            @Override
            public void finish() {
                receivers().each(Receiver::finish);
                receiver.finish();
            }

            private Sequence<Receiver<T>> receivers() {
                return Sequences.sequence(groups.values());
            }
        };
    }
}
