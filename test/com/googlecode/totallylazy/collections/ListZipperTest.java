package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.ImmutableList.constructors.list;
import static com.googlecode.totallylazy.collections.ListZipper.listZipper;
import static com.googlecode.totallylazy.collections.ListZipper.toStart;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ListZipperTest {
    @Test
    public void canGoForward() {
        ListZipper<String> zipper = listZipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.forward().forward();
        assertThat(newZipper.focus(), is(list("C", "D")));
        assertThat(newZipper.breadcrumbs(), is(list("B", "A")));
    }

    @Test
    public void canGoBackward() {
        ListZipper<String> zipper = listZipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.forward().forward().backward();
        assertThat(newZipper.focus(), is(list("B", "C", "D")));
        assertThat(newZipper.breadcrumbs(), is(list("A")));
    }

    @Test
    public void canGoToStart() {
        ListZipper<String> zipper = listZipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.forward().forward();
        assertThat(toStart(newZipper), is(zipper));
    }

    @Test //use case
    public void canUseZipperToAddElementInMiddleOfList() {
        ListZipper<String> zipper = listZipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.forward().forward();
        final ListZipper<String> result = toStart(listZipper(newZipper.focus().cons("Z"), newZipper.breadcrumbs()));
        assertThat(result.focus(), is(list("A", "B", "Z", "C", "D")));
        assertThat(result.breadcrumbs(), is(ImmutableList.constructors.<String>empty()));
    }
}