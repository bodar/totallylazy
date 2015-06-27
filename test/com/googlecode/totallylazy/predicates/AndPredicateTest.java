package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.predicates.Predicates.alwaysFalse;
import static com.googlecode.totallylazy.predicates.Predicates.alwaysTrue;
import static com.googlecode.totallylazy.predicates.Predicates.and;
import static com.googlecode.totallylazy.predicates.Predicates.is;
import static com.googlecode.totallylazy.predicates.Predicates.not;
import static com.googlecode.totallylazy.predicates.Predicates.or;
import static org.hamcrest.MatcherAssert.assertThat;

public class AndPredicateTest {

    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("14"))), Matchers.is(true));
        assertThat(and(is("13"), is("14")).equals(and(is("13"), is("15"))), Matchers.is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(and(is("13"), is("14")).toString(), Matchers.is("(13 and 14)"));
    }

    @Test
    public void collapsesTrues() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> and = and(alwaysTrue(), predicate, alwaysTrue());
        assertThat(and.matches("13"), Matchers.is(predicate.matches("13")));
        assertThat(and.matches("12"), Matchers.is(predicate.matches("12")));
        assertThat(and, Matchers.is(predicate));
    }

    @Test
    public void collapsesFalses() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> and = and(alwaysFalse(), predicate, alwaysFalse());
        assertThat(and.matches("13"), Matchers.is(!predicate.matches("13")));
        assertThat(and.matches("12"), Matchers.is(predicate.matches("12")));
        assertThat(and, Matchers.is(alwaysFalse(String.class)));
    }

    @Test
    public void collapsesAnds() throws Exception {
        assertThat(and(is("12"), and(is("13"), and(is("14"), is("15")))), Matchers.is(and(is("12"), is("13"), is("14"), is("15"))));
    }

    @Test
    public void collapsesEmpty() throws Exception {
        assertThat(Predicates.<String>and().matches("13"), Matchers.is(true));
        assertThat(Predicates.<String>and(), Matchers.is(alwaysTrue(String.class)));
    }

    @Test
    public void collapsesOne() throws Exception {
        LogicalPredicate<String> and = and(is("13"));
        assertThat(and.matches("13"), Matchers.is(true));
        assertThat(and, Matchers.is(is("13")));
    }

    @Test
    public void collapesNots() throws Exception {
        LogicalPredicate<String> original = and(not(is("12")), not(is("13")));
        LogicalPredicate<String> collapsed = not(or(is("12"), is("13")));
        assertThat(original.matches("14"), Matchers.is(collapsed.matches("14")));
        assertThat(original.matches("13"), Matchers.is(collapsed.matches("13")));
        assertThat(original.matches("12"), Matchers.is(collapsed.matches("12")));
        assertThat(original, Matchers.is(collapsed));
    }

}
