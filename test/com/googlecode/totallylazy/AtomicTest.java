package com.googlecode.totallylazy;

import com.googlecode.totallylazy.collections.PersistentSortedMap;
import org.junit.Test;

import static com.googlecode.totallylazy.Atomic.constructors.atomic;
import static com.googlecode.totallylazy.Pair.functions.toPairWithFirst;
import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.emptySortedMap;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.functions;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AtomicTest {
    @Test
    public void canModify() throws Exception {
        assertThat(atomic(sortedMap(3, "Dan")).modify(functions.<Integer, String>remove(3)).value(), is(constructors.<Integer, String>emptySortedMap()));
    }

    @Test
    public void canGetResultAfterModify() throws Exception {
        Atomic<PersistentSortedMap<Integer, String>> reference = atomic(sortedMap(5, "Raymond"));
        assertThat(reference.modifyReturn(functions.<Integer, String>remove(5).then(Pair.functions.<PersistentSortedMap<Integer, String>, String>toPairWithSecond("Removed"))), is("Removed"));
        assertThat(reference.value(), is(emptySortedMap(Integer.class, String.class)));
    }
}