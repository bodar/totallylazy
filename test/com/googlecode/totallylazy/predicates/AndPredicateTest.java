package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicates;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.alwaysFalse;
import static com.googlecode.totallylazy.Predicates.alwaysTrue;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.Assert.assertThat;

public class AndPredicateTest {

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("14"))), is(true));
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("15"))), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(and(is("13"), is("14")).toString(), is("(13 and 14)"));
    }

    @Test
    public void collapsesTrues() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> and = and(alwaysTrue(), predicate, alwaysTrue());
        assertThat(and.matches("13"), is(predicate.matches("13")));
        assertThat(and.matches("12"), is(predicate.matches("12")));
        assertThat(and, equalTo(predicate));
    }

    @Test
    public void collapsesFalses() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> and = and(alwaysFalse(), predicate, alwaysFalse());
        assertThat(and.matches("13"), is(!predicate.matches("13")));
        assertThat(and.matches("12"), is(predicate.matches("12")));
        assertThat(and, equalTo(alwaysFalse(String.class)));
    }

    @Test
    public void collapsesAnds() throws Exception {
        assertThat(and(is("12"), and(is("13"), and(is("14"), is("15")))), equalTo(and(is("12"), is("13"), is("14"), is("15"))));
    }

    @Test
    public void collapsesEmpty() throws Exception {
        assertThat(Predicates.<String>and().matches("13"), is(true));
        assertThat(Predicates.<String>and(), equalTo(alwaysTrue(String.class)));
    }

    @Test
    public void collapsesOne() throws Exception {
        LogicalPredicate<String> and = and(is("13"));
        assertThat(and.matches("13"), is(true));
        assertThat(and, equalTo(is("13")));
    }

    @Test
    public void collapesNots() throws Exception {
        LogicalPredicate<String> original = and(not(is("12")), not(is("13")));
        LogicalPredicate<String> collapsed = not(or(is("12"), is("13")));
        assertThat(original.matches("14"), is(collapsed.matches("14")));
        assertThat(original.matches("13"), is(collapsed.matches("13")));
        assertThat(original.matches("12"), is(collapsed.matches("12")));
        assertThat(original, equalTo(collapsed));
    }

}
