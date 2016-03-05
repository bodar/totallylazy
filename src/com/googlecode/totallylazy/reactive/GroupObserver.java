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
    public State start() {
        observers().each(Observer::start);
        return observer.start();
    }

    @Override
    public State next(T item) {
        groups.computeIfAbsent(keyExtractor.apply(item), k -> {
            Group<K, T> group = new Group<>(k);
            observer.next(group);
            return group;
        }).observe(item);
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
        return sequence(groups.values()).flatMap(Group::observers);
    }
}
