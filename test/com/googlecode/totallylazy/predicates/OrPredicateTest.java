package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.Predicates;
import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Predicates.alwaysFalse;
import static com.googlecode.totallylazy.Predicates.alwaysTrue;
import static com.googlecode.totallylazy.Predicates.and;
import static com.googlecode.totallylazy.Predicates.equalTo;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.or;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;

public class OrPredicateTest {
    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(or(is("13"), is("14")).equals(or(is("13"), is("14"))), is(true));
        assertThat(or(is("13"), is("14")).equals(or(is("13"), is("15"))), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(or(is("13"), is("14")).toString(), is("(13 or 14)"));
    }

    @Test
    public void collapsesTrues() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> or = or(alwaysTrue(), predicate, alwaysTrue());
        assertThat(or, equalTo(alwaysTrue(String.class)));
    }

    @Test
    public void collapsesFalses() throws Exception {
        LogicalPredicate<String> predicate = is("13");
        LogicalPredicate<String> or = or(alwaysFalse(), predicate, alwaysFalse());
        assertThat(or.matches("13"), is(predicate.matches("13")));
        assertThat(or.matches("12"), is(predicate.matches("12")));
        assertThat(or, equalTo(predicate));
    }

    @Test
    public void collapsesOrs() throws Exception {
        assertThat(or(is("12"), or(is("13"), or(is("14"), is("15")))), is(or(is("12"), is("13"), is("14"), is("15"))));
    }

    @Test
    public void collapsesEmpty() throws Exception {
        assertThat(Predicates.<String>or().matches("13"), is(false));
        assertThat(Predicates.<String>or(), equalTo(alwaysFalse(String.class)));
    }

    @Test
    public void collapsesOne() throws Exception {
        LogicalPredicate<String> or = or(is("13"));
        assertThat(or.matches("13"), is(true));
        assertThat(or, is(is("13")));
    }

    @Test
    public void collapesNots() throws Exception {
        LogicalPredicate<String> original = or(not(is("12")), not(is("13")));
        LogicalPredicate<String> collapsed = not(and(is("12"), is("13")));
        assertThat(original.matches("14"), is(collapsed.matches("14")));
        assertThat(original.matches("13"), is(collapsed.matches("13")));
        assertThat(original.matches("12"), is(collapsed.matches("12")));
        assertThat(original, equalTo(collapsed));
    }

}
