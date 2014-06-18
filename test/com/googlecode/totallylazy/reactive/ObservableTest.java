package com.googlecode.totallylazy.reactive;

import org.junit.Test;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.nullValue;
import static com.googlecode.totallylazy.matchers.IterablePredicates.hasExactly;
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
        observable(1, 2, 3).flatMap(i -> observable(i, 0)).subscribe(observer);
        assertObserved(observer, 1, 0, 2, 0, 3, 0);
    }

    private <T> void assertObserved(CapturingObserver<T> observer, T... values) {
        assertThat(observer.items(), hasExactly(values));
        assertThat(observer.error(), nullValue());
        assertThat(observer.completed(), is(true));
    }
}
