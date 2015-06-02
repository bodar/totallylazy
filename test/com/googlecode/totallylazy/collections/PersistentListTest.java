package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;

public class PersistentListTest {
    @Test
    public void supportsLast() throws Exception {
        try {
            list().last();
            fail();
        } catch (NoSuchElementException e) {
            // PASS
        }
        assertThat(list(1).last(), is(1));
        assertThat(list(1, 2).last(), is(2));
        assertThat(list(1, 2, 3).last(), is(3));
    }

    @Test
    public void supportsLastOption() throws Exception {
        assertThat(list().lastOption(), is(Option.none()));
        assertThat(list(1).lastOption(), is(option(1)));
        assertThat(list(1,2).lastOption(), is(option(2)));
        assertThat(list(1,2,3).lastOption(), is(option(3)));
    }

    @Test
    public void supportsTails() throws Exception {
        assertThat(list(1,2,3).tails(), is(list(list(1,2,3),list(2,3),list(3))));
    }




}