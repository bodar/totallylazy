package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Sequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Sequences.sequence;

class GroupObserver<T, K> implements Observer<T> {
    private final Map<K, Group<K, T>> groups = new ConcurrentHashMap<>();
    private final Function<? super T, ? extends K> keyExtractor;
    private final Observer<Group<K, T>> observer;

    GroupObserver(Function<? super T, ? extends K> keyExtractor, Observer<Group<K, T>> observer) {
        this.keyExtractor = keyExtractor;
        this.observer = observer;
    }

    @Override
    public void next(T value) {
        groups.computeIfAbsent(keyExtractor.apply(value), k -> observer.nextR(new Group<>(k))).observe(value);
    }

    @Override
    public void error(Throwable error) {
        observers().each(o -> o.error(error));
        observer.error(error);
    }

    @Override
    public void complete() {
        observers().each(Observer<T>::complete);
        observer.complete();
    }

    private Sequence<Observer<T>> observers() {
        return sequence(groups.values()).flatMap(Group<K, T>::observers);
    }
}
