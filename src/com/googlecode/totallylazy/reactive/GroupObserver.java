package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Sequence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.reactive.Next.next;

class GroupObserver<T, K> implements Observer<T> {
    private final Map<K, Group<K, T>> groups = new ConcurrentHashMap<>();
    private final Function<? super T, ? extends K> keyExtractor;
    private final Observer<Group<K, T>> observer;

    GroupObserver(Function<? super T, ? extends K> keyExtractor, Observer<Group<K, T>> observer) {
        this.keyExtractor = keyExtractor;
        this.observer = observer;
    }

    @Override
    public void step(State<T> state) {
        if(state instanceof Next) {
            groups.computeIfAbsent(keyExtractor.apply(state.value()), k -> {
                Group<K, T> group = new Group<>(k);
                observer.step(next(group));
                return group;
            }).observe(state);
        }
        if(state instanceof Error){
            observers().each(o -> o.step(state));
            observer.step(Error.error(((Error) state).throwable()));
        }
        if(state instanceof Complete){
            observers().each(o -> o.step(state));
            observer.step(Complete.complete());
        }
    }

    private Sequence<Observer<T>> observers() {
        return sequence(groups.values()).flatMap(Group<K, T>::observers);
    }
}
