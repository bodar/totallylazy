package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Atomic.constructors.atomic;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.functions;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AtomicTest {
    @Test
    public void canModify() throws Exception {
        assertThat(atomic(sortedMap(3, "Dan")).modify(functions.<Integer, String>remove(3)).value(), is(constructors.<Integer, String>emptySortedMap()));
    }
}
