package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Predicates.between;
import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.matchers.Matchers.matcher;
import static org.junit.Assert.assertThat;

public class RandomsTest {
    @Test
    public void integers() {
        assertThat(Randoms.integers().take(5), matcher(Predicates.<Integer>forAll(between(Integer.MIN_VALUE, Integer.MAX_VALUE))));
    }

    @Test
    public void integerRange() {
        assertThat(Randoms.range(-5, 10).take(5), matcher(Predicates.<Integer>forAll(between(-5, 10))));
        assertThat(Randoms.range(1, 1).take(5), matcher(Predicates.<Integer>forAll(equalTo(1))));
    }

    @Test(expected = IllegalArgumentException.class)
    public void integerRangeWithInvalidValues() {
        Randoms.range(10, -5);
    }

    @Test
    public void longs() {
        assertThat(Randoms.longs().take(5), matcher(Predicates.<Long>forAll(between(Long.MIN_VALUE, Long.MAX_VALUE))));
    }

    @Test
    public void booleans() {
        assertThat(Randoms.booleans().take(5), matcher(Predicates.<Boolean>forAll(or(is(true), is(false)))));
    }

    @Test
    public void dates() {
        assertThat(Randoms.dates().take(5).map(getTime()), matcher(Predicates.<Long>forAll(between(Long.MIN_VALUE, Long.MAX_VALUE))));
    }

    private Function1<Date, Long> getTime() {
        return new Function1<Date, Long>() {
            @Override
            public Long call(Date date) throws Exception {
                return date.getTime();
            }
        };
    }
}