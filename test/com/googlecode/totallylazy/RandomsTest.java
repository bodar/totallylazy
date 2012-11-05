package com.googlecode.totallylazy;

import com.googlecode.totallylazy.time.Dates;
import com.googlecode.totallylazy.time.Days;
import org.junit.Test;

import java.util.Date;

import static com.googlecode.totallylazy.Predicates.between;
import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Predicates.in;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.matchers.Matchers.matcher;
import static org.junit.Assert.assertThat;

public class RandomsTest {
    private static final int TESTS = 100;
    private static final Date NOW = Dates.date(2000, 1, 2);

    @Test
    public void integers() {
        assertThat(Randoms.integers().take(TESTS), matcher(Predicates.<Integer>forAll(between(Integer.MIN_VALUE, Integer.MAX_VALUE))));
    }

    @Test
    public void longs() {
        assertThat(Randoms.longs().take(TESTS), matcher(Predicates.<Long>forAll(between(Long.MIN_VALUE, Long.MAX_VALUE))));
    }

    @Test
    public void doubles() {
        assertThat(Randoms.doubles().take(TESTS), matcher(Predicates.<Double>forAll(between(Double.MIN_VALUE, Double.MAX_VALUE))));
    }

    @Test
    public void booleans() {
        assertThat(Randoms.booleans().take(TESTS), matcher(Predicates.<Boolean>forAll(or(is(true), is(false)))));
    }

    @Test
    public void dates() {
        assertThat(Randoms.dates().take(TESTS), matcher(Predicates.<Date>forAll(between(Dates.MIN_VALUE, Dates.MAX_VALUE))));
    }

    @Test
    public void values() {
        assertThat(Randoms.values(1,2,3,4,5).take(TESTS), matcher(Predicates.<Integer>forAll(in(1,2,3,4,5))));
    }

    @Test
    public void betweenIntegers() {
        assertThat(Randoms.between(-5, 10).take(TESTS), matcher(Predicates.<Integer>forAll(between(-5, 10))));
        assertThat(Randoms.between(10, -5).take(TESTS), matcher(Predicates.<Integer>forAll(between(-5, 10))));
        assertThat(Randoms.between(1, 1).take(TESTS), matcher(Predicates.<Integer>forAll(equalTo(1))));
    }

    @Test
    public void betweenDoubles() {
        assertThat(Randoms.between(-5.4, 10.1).take(TESTS), matcher(Predicates.<Double>forAll(between(-5.4, 10.1))));
        assertThat(Randoms.between(10.1, -5.4).take(TESTS), matcher(Predicates.<Double>forAll(between(-5.4, 10.1))));
        assertThat(Randoms.between(1.0, 1.0).take(TESTS), matcher(Predicates.<Double>forAll(equalTo(1.0))));
    }

    @Test
    public void betweenDates() {
        Date yesterday = Days.subtract(NOW, 1);
        Date tomorrow = Days.add(NOW, 1);
        assertThat(Randoms.between(yesterday, tomorrow).take(TESTS), matcher(Predicates.<Date>forAll(between(yesterday, tomorrow))));
        assertThat(Randoms.between(tomorrow, yesterday).take(TESTS), matcher(Predicates.<Date>forAll(between(yesterday, tomorrow))));
        assertThat(Randoms.between(NOW, NOW).take(TESTS), matcher(Predicates.<Date>forAll(between(NOW, NOW))));
    }

    @Test
    public void datesAfter() {
        assertThat(Randoms.after(NOW).take(TESTS), matcher(Predicates.<Date>forAll(between(NOW, Dates.MAX_VALUE))));
    }
}