package com.googlecode.totallylazy;

import com.googlecode.totallylazy.matchers.NumberMatcher;
import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Callables.callThrows;
import static com.googlecode.totallylazy.Callables.ignoreAndReturn;
import static com.googlecode.totallylazy.Callables.returns;
import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Functions.constant;
import static com.googlecode.totallylazy.Objects.equalTo;
import static com.googlecode.totallylazy.Option.applicate;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.OptionTest.Person.person;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Sequences.size;
import static com.googlecode.totallylazy.matchers.IterableMatcher.hasExactly;
import static com.googlecode.totallylazy.numbers.Numbers.add;
import static com.googlecode.totallylazy.numbers.Numbers.divide;
import static com.googlecode.totallylazy.numbers.Numbers.number;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

public class OptionTest {
    @Test
    public void supportsContains() throws Exception {
        assertThat(option(1).contains(1), is(true));
        assertThat(option(1).contains(2), is(false));
    }

    @Test
    public void supportsIsAlias() throws Exception {
        assertThat(option(1).is(Predicates.is(1)), is(true));
        assertThat(option(1).is(Predicates.is(2)), is(false));
    }

    @Test
    public void supportsExists() throws Exception {
        assertThat(option(1).exists(Predicates.is(1)), is(true));
        assertThat(option(1).exists(Predicates.is(2)), is(false));
    }

    @Test
    public void supportsApplicativeEquality() throws Exception {
        final Option<CurriedFunction2<Object, Object, Boolean>> some = some(equalTo());
        final Option<Integer> some1 = some(3);
        final Option<Function1<Object, Boolean>> applicate = applicate(some, some1);
        assertThat(applicate(applicate, some(5)), is(some(false)));
        assertThat(applicate(applicate(some(equalTo()), some(3)), some(3)), is(some(true)));
        assertThat(applicate(applicate(some(equalTo()), none(Integer.class)), some(3)), is(none(Boolean.class)));
        assertThat(applicate(applicate(some(equalTo()), some(3)), none(Integer.class)), is(none(Boolean.class)));
    }

    @Test
    public void supportsApplicativeUsage() throws Exception {
        assertThat(none(Number.class).applicate(some(add(3))), is(none(Number.class)));
        assertThat(some(9).applicate(Option.<Function1<Number, Number>>none()), is(none(Number.class)));
        assertThat(some(9).applicate(some(add(3))), is(Option.<Number>some(12)));

        assertThat(some(5).applicate(some(3).applicate(some(add()))), is(Option.<Number>some(8)));
        assertThat(none(Number.class).applicate(some(3).applicate(some(add()))), is(none(Number.class)));
        assertThat(some(5).applicate(none(Number.class).applicate(some(add()))), is(none(Number.class)));

        assertThat(applicate(applicate(some(add()), some(3)), some(5)), is(Option.<Number>some(8)));
        assertThat(applicate(applicate(some(add()), none(Number.class)), some(5)), is(none(Number.class)));
        assertThat(applicate(applicate(some(add()), some(3)), none(Number.class)), is(none(Number.class)));
    }

    @Test
    public void supportsApplicativeUsageToConstruct() throws Exception {
        assertThat(some("Dan").applicate(some(35).applicate(some(person().flip()))), is(some(person("Dan", 35))));
        assertThat(some("Ray").applicate(none(Integer.class).applicate(some(person().flip()))), is(none(Person.class)));
        assertThat(none(String.class).applicate(some(100).applicate(some(person().flip()))), is(none(Person.class)));

        assertThat(applicate(applicate(some(person()), some("Dan")), some(35)), is(some(person("Dan", 35))));
        assertThat(applicate(applicate(some(person()), some("Ray")), none(Integer.class)), is(none(Person.class)));
        assertThat(applicate(applicate(some(person()), none(String.class)), some(100)), is(none(Person.class)));
    }

    @Test
    public void supportsJoin() {
        assertThat(some(1).join(sequence(2, 3)), hasExactly(1, 2, 3));
        assertThat(none(Integer.class).join(sequence(1, 2, 3)), hasExactly(1, 2, 3));
    }

    static class Person {
        private final String name;
        private final int age;

        private Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        static CurriedFunction2<String, Integer, Person> person() {
            return Person::person;
        }

        static Person person(String name, int age) {
            return new Person(name, age);
        }

        private Sequence<Object> values() {
            return Sequences.<Object>sequence(name, age);
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Person && ((Person) o).values().equals(values());
        }

        @Override
        public int hashCode() {
            return values().hashCode();
        }
    }

    @Test
    public void canFold() throws Exception {
        assertThat(option(1).fold(1, add()), NumberMatcher.is(2));
        assertThat(some(1).fold(1, add()), NumberMatcher.is(2));
        assertThat(Option.<Number>none().fold(1, add()), NumberMatcher.is(1));
    }

    @Test
    public void canMap() throws Exception {
        assertThat(option(1).map(toString), is(option("1")));
        assertThat(some(2).map(toString), is(some("2")));
        assertThat(none().map(toString), is(none(String.class)));
        assertThat(some(2).map(ignoreAndReturn(null)), is(none()));
    }

    @Test
    public void canFlatMap() {
        assertThat(some(number(4)).flatMap(divide(2).optional()), is(some((Number) 2)));
        assertThat(some(number(4)).flatMap(divide(0).optional()), is(none(Number.class)));
        assertThat(none(Number.class).flatMap(constant(none(Number.class))), is(none(Number.class)));
        assertThat(none(Number.class).flatMap(constant(some(number(4)))), is(none(Number.class)));
    }

    @Test
    public void canFlatten() {
        assertThat(Option.flatten(some(some(1))), is(some(1)));
        assertThat(Option.flatten(some(none())), is(none()));
    }

    @Test
    public void areIterable() throws Exception {
        assertThat(size(some(1)), NumberMatcher.is(1));
        assertThat(size(none()), NumberMatcher.is(0));
    }

    @Test
    public void canGetValueOfSome() throws Exception {
        assertThat(some(1).get(), is(1));
    }

    @Test(expected = NoSuchElementException.class)
    public void cannotGetValueOfNone() throws Exception {
        none().get();
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

    @Test(expected = RuntimeException.class)
    public void canThrowIfNone()  throws Exception{
        assertThat(some("bob").getOrThrow(new RuntimeException()), is("bob"));
        none().getOrThrow(new RuntimeException());
    }

    @Test
    public void canConvertSomeToEitherAsRight() {
        assertThat(some("all good").<String>toEither("borked"), is(Either.<String, String>right("all good")));
    }

    @Test
    public void canConvertNoneToEitherAsLeft() {
        assertThat(none().toEither("borked"), is(Either.<String, Object>left("borked")));
    }
}
