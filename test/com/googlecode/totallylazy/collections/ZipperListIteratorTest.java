package com.googlecode.totallylazy.collections;

import org.junit.Test;

import java.util.Iterator;
import java.util.ListIterator;

import static com.googlecode.totallylazy.collections.ListZipper.zipper;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class ZipperListIteratorTest {
    @Test
    public void canGoForward() throws Exception {
        PersistentList<Character> values = list('a', 'b', 'c', 'd', 'e');
        Iterator<Character> expected = values.iterator();
        ListIterator<Character> listIterator = new ZipperListIterator<Character>(zipper(values));

        for (int i = 0; i < values.size(); i++) {
            assertThat(listIterator.hasNext(), is(expected.hasNext()));
            assertThat(listIterator.next(), is(expected.next()));
        }

        assertThat(listIterator.hasNext(), is(expected.hasNext()));
    }

    @Test
    public void canGoBackwards() throws Exception {
        PersistentList<Character> values = list('a', 'b', 'c', 'd', 'e');
        Iterator<Character> expected = values.reverse().iterator();
        ListIterator<Character> listIterator = new ZipperListIterator<Character>(zipper(values).last());

        for (int i = 0; i < values.size(); i++) {
            assertThat(listIterator.hasPrevious(), is(expected.hasNext()));
            assertThat(listIterator.previous(), is(expected.next()));
        }

        assertThat(listIterator.hasPrevious(), is(expected.hasNext()));
    }

    @Test
    public void canGoBackwardsAndForwards() throws Exception {
        PersistentList<Character> values = list('a', 'b', 'c');
        ListIterator<Character> listIterator = new ZipperListIterator<Character>(zipper(values));

        assertThat(listIterator.next(), is('a'));
        assertThat(listIterator.next(), is('b'));
        assertThat(listIterator.next(), is('c'));
        assertThat(listIterator.previous(), is('c'));
        assertThat(listIterator.next(), is('c'));
        assertThat(listIterator.previous(), is('c'));
        assertThat(listIterator.previous(), is('b'));
        assertThat(listIterator.previous(), is('a'));
    }

    @Test
    public void supportsIndex() throws Exception {
        PersistentList<Character> values = list('a', 'b', 'c');
        ListIterator<Character> listIterator = new ZipperListIterator<Character>(zipper(values));

        assertThat(listIterator.nextIndex(), is(0));
        assertThat(listIterator.previousIndex(), is(-1));

        assertThat(listIterator.next(), is('a'));
        assertThat(listIterator.nextIndex(), is(1));
        assertThat(listIterator.previousIndex(), is(0));

        assertThat(listIterator.next(), is('b'));
        assertThat(listIterator.nextIndex(), is(2));
        assertThat(listIterator.previousIndex(), is(1));

        assertThat(listIterator.next(), is('c'));
        assertThat(listIterator.nextIndex(), is(3));
        assertThat(listIterator.previousIndex(), is(2));

        assertThat(listIterator.previous(), is('c'));
        assertThat(listIterator.nextIndex(), is(2));
        assertThat(listIterator.previousIndex(), is(1));

        assertThat(listIterator.next(), is('c'));
        assertThat(listIterator.nextIndex(), is(3));
        assertThat(listIterator.previousIndex(), is(2));

        assertThat(listIterator.previous(), is('c'));
        assertThat(listIterator.nextIndex(), is(2));
        assertThat(listIterator.previousIndex(), is(1));

        assertThat(listIterator.previous(), is('b'));
        assertThat(listIterator.nextIndex(), is(1));
        assertThat(listIterator.previousIndex(), is(0));

        assertThat(listIterator.previous(), is('a'));
        assertThat(listIterator.nextIndex(), is(0));
        assertThat(listIterator.previousIndex(), is(-1));
    }
}
