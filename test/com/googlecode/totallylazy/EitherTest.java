package com.googlecode.totallylazy;

import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Left.left;
import static com.googlecode.totallylazy.Right.right;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class EitherTest {
    @Test
    public void supportsCreatingRights() throws Exception {
        Either<Exception, Integer> either = right(3);
        assertThat(either.isRight(), is(true));
        assertThat(either.isLeft(), is(false));

        assertThat(either.right(), is(3));
    }

    @Test
    public void supportsCreatingLefts() throws Exception {
        final NumberFormatException exception = new NumberFormatException("waah");
        Either<NumberFormatException, Integer> either = left(exception);
        assertThat(either.isRight(), is(false));
        assertThat(either.isLeft(), is(true));

        assertThat(either.left(), is(exception));
    }

    @Test(expected = NoSuchElementException.class)
    public void doesNotSupportGettingLeftFromARight() throws Exception {
        Either<Exception, Integer> either = right(3);
        either.left();
    }

    @Test(expected = NoSuchElementException.class)
    public void doesNotSupportGettingRightFromALeft() throws Exception {
        Either<RuntimeException, Integer> either = left(new RuntimeException());
        either.right();
    }


}