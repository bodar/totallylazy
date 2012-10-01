package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.ImmutableList.constructors.list;
import static com.googlecode.totallylazy.collections.ImmutableList.functions.cons;
import static com.googlecode.totallylazy.collections.ImmutableList.functions.tail;
import static com.googlecode.totallylazy.collections.ListZipper.toStart;
import static com.googlecode.totallylazy.collections.ListZipper.zipper;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ListZipperTest {
    @Test
    public void canGoRight() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.right().right();
        assertThat(newZipper.focus, is(list("C", "D")));
        assertThat(newZipper.breadcrumbs, is(list("B", "A")));
    }

    @Test
    public void canGoLeft() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.right().right().left();
        assertThat(newZipper.focus, is(list("B", "C", "D")));
        assertThat(newZipper.breadcrumbs, is(list("A")));
    }

    @Test
    public void canGoToStart() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.right().right();
        assertThat(toStart(newZipper), is(zipper));
    }

    @Test
    public void canUseZipperToAddElementInMiddleOfList() {
        final ImmutableList<String> modified = zipper(list("A", "B", "C", "D")).right().right().modify(cons("Z")).toList();
        assertThat(modified, is(list("A", "B", "Z", "C", "D")));
        final ImmutableList<String> inserted = zipper(list("A", "B", "C", "D")).right().right().insert("Z").toList();
        assertThat(inserted, is(list("A", "B", "Z", "C", "D")));
    }

    @Test
    public void canUseZipperToRemoveElementInMiddleOfList() {
        final ImmutableList<String> modified = zipper(list("A", "B", "C", "D")).right().right().modify(tail(String.class)).toList();
        assertThat(modified, is(list("A", "B", "D")));
        final ImmutableList<String> deleted = zipper(list("A", "B", "C", "D")).right().right().delete().toList();
        assertThat(deleted, is(list("A", "B", "D")));
    }

    @Test
    public void canPerformMultipleModifications() throws Exception {
        final ImmutableList<String> modified = zipper(list("A", "B", "C", "D")).right().delete().delete().toList();
        assertThat(modified, is(list("A", "D")));
    }

    @Test
    public void supportsCurrent() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.current(), is("A"));
        assertThat(zipper.right().current(), is("B"));
    }

    @Test
    public void supportsAtEnd() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.atEnd(), is(false));
        assertThat(zipper.right().right().atEnd(), is(true));
    }

    @Test
    public void supportsAtStart() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.atStart(), is(true));
        assertThat(zipper.right().atStart(), is(false));
    }
}