package com.googlecode.totallylazy.reactive;

import com.googlecode.totallylazy.numbers.Numbers;
import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
import static com.googlecode.totallylazy.numbers.Numbers.average;
import static com.googlecode.totallylazy.reactive.Observable.observable;

public class ObservableTest {
    @Test
    public void canObserverItems() throws Exception {
        assertObserved(observable(1, 2, 3), 1, 2, 3);
    }

    @Test
    public void supportsFiltering() throws Exception {
        assertObserved(observable(1, 2, 3).filter(i -> i % 2 == 0), 2);
    }

    @Test
    public void supportsMapping() throws Exception {
        assertObserved(observable(1, 2, 3).map(Object::toString), "1", "2", "3");
    }

    @Test
    public void supportsFlatMapping() throws Exception {
        assertObserved(observable(1, 2, 3).flatMap((Integer i) -> observable(i, i * 2)), 1, 2, 2, 4, 3, 6);
    }

    @Test
    public void supportsScan() throws Exception {
        assertObserved(observable(0, 2, 4).scan(average).map(Number::intValue), 0, 1, 2);
    }

    @Test
    public void supportsLast() throws Exception {
        assertObserved(observable(0, 2, 4).last(), 4);
    }

    @Test
    public void supportsReduce() throws Exception {
        assertObserved(observable(0, 2, 4).reduce(average).map(Number::intValue),
                2);
    }

    @Test
    public void supportsTake() throws Exception {
        assertObserved(observable(1, 2, 3, 4, 5, 6).take(3),
                1, 2, 3);
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        assertObserved(observable(1, 2, 3, 4, 5, 6).takeWhile(i -> i < 4),
                1, 2, 3);
    }

    @Test
    public void supportsDrop() throws Exception {
        assertObserved(observable(1, 2, 3, 4, 5, 6).drop(3),
                4, 5, 6);
    }

    @Test
    public void supportsDropWhile() throws Exception {
        assertObserved(observable(1, 2, 3, 4, 5, 6).dropWhile(i -> i < 4),
                4, 5, 6);
    }


    @SafeVarargs
    private final <T> void assertObserved(Observable<T> observable, T... values) {
        CapturingObserver<T> observer = new CapturingObserver<>();
        observable.subscribe(observer);
        assertThat(observer.items(), hasExactly(values));
        assertThat(observer.error(), nullValue());
        assertThat(observer.completed(), is(true));
    }
}
