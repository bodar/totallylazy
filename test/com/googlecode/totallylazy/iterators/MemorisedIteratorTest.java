package com.googlecode.totallylazy.iterators;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Sequences.sequence;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class MemorisedIteratorTest {
    @Test
    public void supportsListIterator() throws Exception {
        List<Integer> memory = new ArrayList<Integer>();
        Iterator<Integer> iterator = sequence(100, 200, 300, 400).iterator();
        ListIterator<Integer> memorisedIterator = new MemorisedIterator<Integer>(iterator, memory);

        assertThat(memorisedIterator.previousIndex(), is(-1));
        assertThat(memorisedIterator.nextIndex(), is(0));
        assertThat(memorisedIterator.hasNext(), is(true));
        assertThat(memorisedIterator.hasPrevious(), is(false));

        assertThat(memorisedIterator.next(), is(100));
        assertThat(memorisedIterator.previous(), is(100));

        assertThat(memorisedIterator.previousIndex(), is(-1));
        assertThat(memorisedIterator.nextIndex(), is(0));
        assertThat(memorisedIterator.hasNext(), is(true));
        assertThat(memorisedIterator.hasPrevious(), is(false));

        assertThat(memorisedIterator.next(), is(100));
        assertThat(memorisedIterator.previous(), is(100));

        assertThat(memorisedIterator.previousIndex(), is(-1));
        assertThat(memorisedIterator.nextIndex(), is(0));
        assertThat(memorisedIterator.hasNext(), is(true));
        assertThat(memorisedIterator.hasPrevious(), is(false));

        try{
            assertThat(memorisedIterator.hasPrevious(), is(false));
            memorisedIterator.previous();
            fail("Should have thrown NoSuchElementException");
        } catch (NoSuchElementException e){
            // All good
        }

        assertThat(memorisedIterator.next(), is(100));
        assertThat(memorisedIterator.next(), is(200));

        assertThat(memorisedIterator.previousIndex(), is(1));
        assertThat(memorisedIterator.nextIndex(), is(2));
        assertThat(memorisedIterator.hasNext(), is(true));
        assertThat(memorisedIterator.hasPrevious(), is(true));

        assertThat(memorisedIterator.next(), is(300));
        assertThat(memorisedIterator.next(), is(400));

        assertThat(memorisedIterator.previousIndex(), is(3));
        assertThat(memorisedIterator.nextIndex(), is(4));
        assertThat(memorisedIterator.hasNext(), is(false));
        assertThat(memorisedIterator.hasPrevious(), is(true));

        assertThat(memorisedIterator.previous(), is(400));
        assertThat(memorisedIterator.previous(), is(300));
        assertThat(memorisedIterator.previous(), is(200));
        assertThat(memorisedIterator.previous(), is(100));

        assertThat(memorisedIterator.previousIndex(), is(-1));
        assertThat(memorisedIterator.nextIndex(), is(0));
        assertThat(memorisedIterator.hasNext(), is(true));
        assertThat(memorisedIterator.hasPrevious(), is(false));
    }
}
