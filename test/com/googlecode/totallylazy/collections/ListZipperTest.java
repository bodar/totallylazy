package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentList.functions.cons;
import static com.googlecode.totallylazy.collections.PersistentList.functions.tail;
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
    public void canGoToTop() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.right().right();
        assertThat(newZipper.top(), is(zipper));
    }

    @Test
    public void canUseZipperToAddElementInMiddleOfList() {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).right().right().modify(cons("Z")).toList();
        assertThat(modified, is(list("A", "B", "Z", "C", "D")));
        final PersistentList<String> inserted = zipper(list("A", "B", "C", "D")).right().right().insert("Z").toList();
        assertThat(inserted, is(list("A", "B", "Z", "C", "D")));
    }

    @Test
    public void canUseZipperToRemoveElementInMiddleOfList() {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).right().right().modify(tail(String.class)).toList();
        assertThat(modified, is(list("A", "B", "D")));
        final PersistentList<String> deleted = zipper(list("A", "B", "C", "D")).right().right().delete().toList();
        assertThat(deleted, is(list("A", "B", "D")));
    }

    @Test
    public void canPerformMultipleModifications() throws Exception {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).right().delete().delete().toList();
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
        assertThat(zipper.isBottom(), is(false));
        assertThat(zipper.right().right().isBottom(), is(true));
    }

    @Test
    public void supportsAtStart() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.isTop(), is(true));
        assertThat(zipper.right().isTop(), is(false));
    }

    @Test
    public void supportsIndex() throws Exception {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        assertThat(zipper.value(), is("A"));
        assertThat(zipper.index(), is(0));
        zipper = zipper.next();
        assertThat(zipper.value(), is("B"));
        assertThat(zipper.index(), is(1));
        zipper = zipper.next();
        assertThat(zipper.value(), is("C"));
        assertThat(zipper.index(), is(2));
        zipper = zipper.next();
        assertThat(zipper.value(), is("D"));
        assertThat(zipper.index(), is(3));
    }
}