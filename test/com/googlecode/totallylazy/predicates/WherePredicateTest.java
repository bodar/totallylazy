package com.googlecode.totallylazy.predicates;

import com.googlecode.totallylazy.matchers.Matchers;
import org.junit.Test;

import static com.googlecode.totallylazy.Callables.toString;
import static com.googlecode.totallylazy.Predicates.always;
import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.never;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.predicates.WherePredicate.where;
import static com.googlecode.totallylazy.PredicateAssert.assertThat;
import static com.googlecode.totallylazy.Predicates.instanceOf;

public class WherePredicateTest {
    @Test
    public void supportsEqualityOnPredicateItself() throws Exception {
        assertThat(where(toString, is("13")).equals(where(toString, is("13"))), is(true));
        assertThat(where(toString, is("13")).equals(where(toString, is("14"))), is(false));
    }

    @Test
    public void supportsToString() throws Exception {
        assertThat(where(toString, is("13")).toString(), is("where toString is 13"));
    }

    @Test
    public void combiningAWhereWithAlwaysReturnsAnAlways() throws Exception {
        assertThat(where(toString, always()), instanceOf(always().getClass()));
    }

    @Test
    public void combiningAWhereWithNeverReturnsANever() throws Exception {
        assertThat(where(toString, never()), instanceOf(never().getClass()));
    }

    @Test
    public void supportsDeMorganLawByMovingNotToOutside() throws Exception {
        assertThat(where(toString, not(is("Dan"))), is(not(where(toString, is("Dan")))));
    }
}
