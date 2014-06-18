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
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(1, 2, 3).subscribe(observer);
        assertObserved(observer, 1, 2, 3);
    }

    @Test
    public void supportsFiltering() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(1, 2, 3).filter(i -> i % 2 == 0).subscribe(observer);
        assertObserved(observer, 2);
    }

    @Test
    public void supportsMapping() throws Exception {
        CapturingObserver<String> observer = new CapturingObserver<>();
        observable(1, 2, 3).map(Object::toString).subscribe(observer);
        assertObserved(observer, "1", "2", "3");
    }

    @Test
    public void supportsFlatMapping() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(1, 2, 3).flatMap(i -> observable(i, i * 2)).subscribe(observer);
        assertObserved(observer, 1, 2, 2, 4, 3, 6);
    }

    @Test
    public void supportsScan() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(0, 2, 4).scan(average).map(Number::intValue).subscribe(observer);
        assertObserved(observer, 0, 1, 2);
    }

    @Test
    public void supportsLast() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(0, 2, 4).last().subscribe(observer);
        assertObserved(observer, 4);
    }

    @Test
    public void supportsReduce() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(0, 2, 4).reduce(average).map(Number::intValue).subscribe(observer);
        assertObserved(observer, 2);
    }

    @Test
    public void supportsTake() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(1, 2, 3, 4, 5, 6).take(3).subscribe(observer);
        assertObserved(observer, 1, 2, 3);
    }

    @Test
    public void supportsTakeWhile() throws Exception {
        CapturingObserver<Integer> observer = new CapturingObserver<>();
        observable(1, 2, 3, 4, 5, 6).takeWhile(i -> i < 4).subscribe(observer);
        assertObserved(observer, 1, 2, 3);
    }

    @SafeVarargs
    private final <T> void assertObserved(CapturingObserver<T> observer, T... values) {
        assertThat(observer.items(), hasExactly(values));
        assertThat(observer.error(), nullValue());
        assertThat(observer.completed(), is(true));
    }
}
