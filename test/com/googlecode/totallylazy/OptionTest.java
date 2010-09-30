package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Callables.asString;
import static com.googlecode.totallylazy.Callables.call;
import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.size;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class OptionTest {
    @Test
    public void canMap() throws Exception {
        assertThat(option(1).map(asString()), is(option("1")));
        assertThat(some(2).map(asString()), is((Option)some("2")));
        assertThat(none().map(asString()), is((Option)none()));
    }

    @Test
    public void areIterable() throws Exception {
        assertThat(size(some(1)), is(1));
        assertThat(size(none()), is(0));
    }

    @Test
    public void canGetValue() throws Exception {
        assertThat(some(1).get(), is(1));

        try {
            none().get();
            fail();
        } catch (NoSuchElementException e) {
            
        }
    }

    @Test
    public void canGetOrElseValue() throws Exception {
        assertThat(some(1).getOrElse(2), is(1));
        assertThat(Option.<Integer>none().getOrElse(2), is(2));
        assertThat(option(1).getOrElse(2), is(1));
        assertThat(Option.<Integer>option(null).getOrElse(2), is(2));
    }

    @Test
    public void canGetOrElseWithCallable() throws Exception {
        assertThat(some(1).getOrElse(returns(2)), is(1));
        assertThat(Option.<Integer>none().getOrElse(returns(2)), is(2));
        assertThat(option(1).getOrElse(returns(2)), is(1));
        assertThat(Option.<Integer>option(null).getOrElse(returns(2)), is(2));

        try {
            assertThat(Option.<Integer>option(null).getOrElse(callThrows(new RuntimeException(), Integer.class)), is(2));
            fail();
        } catch (RuntimeException e) {

        }
    }

    @Test
    public void canGetOrNullValue() throws Exception {
        assertThat(some(1).getOrNull(), is(1));
        assertThat(Option.<Integer>none().getOrNull(), is(nullValue(Integer.class)));
        assertThat(option(1).getOrNull(), is(1));
        assertThat(Option.<Integer>option(null).getOrNull(), is(nullValue(Integer.class)));
    }

    @Test
    public void canSeeIfEmpty() throws Exception {
        assertThat(some(1).isEmpty(), is(false));
        assertThat(none().isEmpty(), is(true));
    }
}
