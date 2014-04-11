package com.googlecode.totallylazy;

import org.junit.Test;

import static com.googlecode.totallylazy.Monad.methods.sequenceE;
import static com.googlecode.totallylazy.Monad.methods.sequenceEs;
import static com.googlecode.totallylazy.Monad.methods.sequenceO;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.Sequences.empty;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.numbers.Numbers.numbers;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class MonadTest {
    @Test
    public void canSwapEitherContainerAndGetAllErrors() throws Exception {
        assertThat(sequenceEs(sequence(Either.<String, Number>right(3), Either.<String, Long>right(4L))),
                is(Either.<Seq<String>, Seq<Number>>right(numbers(3, 4L))));
        assertThat(sequenceEs(sequence(Either.<String, Number>right(3), Either.<String, Number>left("error1"), Either.<String, Number>left("error2"))),
                is(Either.<Seq<String>, Seq<Number>>left(sequence("error1", "error2"))));
        assertThat(sequenceEs(Sequences.<Either<String, Number>>empty()),
                is(Either.<Seq<String>, Seq<Number>>right(empty(Number.class))));
    }

    @Test
    public void canSwapEitherContainer() throws Exception {
        assertThat(sequenceE(sequence(Either.<String, Number>right(3), Either.<String, Long>right(4L))),
                is(Either.<String, Seq<Number>>right(numbers(3, 4L))));
        assertThat(sequenceE(sequence(Either.<String, Number>right(3), Either.<String, Number>left("error"))),
                is(Either.<String, Seq<Number>>left("error")));
        assertThat(sequenceE(Sequences.<Either<String, Number>>empty()),
                is(Either.<String, Seq<Number>>right(empty(Number.class))));
    }

    @Test
    public void canSwapOptionContainer() throws Exception {
        assertThat(sequenceO(sequence(some(3), some(4))),
                is(some(sequence(3, 4))));
        assertThat(sequenceO(sequence(some(3), none(Integer.class))),
                is(Option.<Seq<Integer>>none()));
        assertThat(sequenceO(Sequences.<Option<Number>>empty()),
                is(Option.some(empty(Number.class))));
    }
}
