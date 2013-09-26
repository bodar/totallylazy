package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.ListZipper.zipper;
import static com.googlecode.totallylazy.collections.PersistentList.constructors.list;
import static com.googlecode.totallylazy.collections.PersistentList.functions.cons;
import static com.googlecode.totallylazy.collections.PersistentList.functions.tail;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ListZipperTest {
    @Test
    public void supportsSingleList() {
        ListZipper<String> zipper = zipper(list("A"));
        assertThat(zipper.nextOption().isEmpty(), is(true));
        assertThat(zipper.previousOption().isEmpty(), is(true));
        assertThat(zipper.isFirst(), is(true));
        assertThat(zipper.isLast(), is(true));
    }



    @Test
    public void supportsNext() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.next().next();
        assertThat(newZipper.focus, is(list("C", "D")));
        assertThat(newZipper.breadcrumbs, is(list("B", "A")));
    }

    @Test
    public void supportsPrevious() {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        final ListZipper<String> newZipper = zipper.next().next().previous();
        assertThat(newZipper.focus, is(list("B", "C", "D")));
        assertThat(newZipper.breadcrumbs, is(list("A")));
    }

    @Test
    public void supportsLast() throws Exception {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D")).last();
        assertThat(zipper.value(), is("D"));
        assertThat(zipper.isLast(), is(true));
    }

    @Test
    public void supportsFirst() throws Exception {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        assertThat(zipper.value(), is("A"));
        assertThat(zipper.isFirst(), is(true));
        zipper= zipper.first();
        assertThat(zipper.value(), is("A"));
        assertThat(zipper.isFirst(), is(true));
    }


    @Test
    public void canUseZipperToAddElementInMiddleOfList() {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).next().next().modify(cons("Z")).toList();
        assertThat(modified, is(list("A", "B", "Z", "C", "D")));
        final PersistentList<String> inserted = zipper(list("A", "B", "C", "D")).next().next().insert("Z").toList();
        assertThat(inserted, is(list("A", "B", "Z", "C", "D")));
    }

    @Test
    public void canUseZipperToRemoveElementInMiddleOfList() {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).next().next().modify(tail(String.class)).toList();
        assertThat(modified, is(list("A", "B", "D")));
        final PersistentList<String> deleted = zipper(list("A", "B", "C", "D")).next().next().delete().toList();
        assertThat(deleted, is(list("A", "B", "D")));
    }

    @Test
    public void canPerformMultipleModifications() throws Exception {
        final PersistentList<String> modified = zipper(list("A", "B", "C", "D")).next().delete().delete().toList();
        assertThat(modified, is(list("A", "D")));
    }

    @Test
    public void supportsCurrent() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.current(), is("A"));
        assertThat(zipper.next().current(), is("B"));
    }

    @Test
    public void supportsAtEnd() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.current(), is("A"));
        assertThat(zipper.isLast(), is(false));
        zipper = zipper.next();
        assertThat(zipper.current(), is("B"));
        assertThat(zipper.isLast(), is(true));
    }

    @Test
    public void supportsAtStart() {
        ListZipper<String> zipper = zipper(list("A", "B"));
        assertThat(zipper.isTop(), is(true));
        assertThat(zipper.next().isTop(), is(false));
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

    @Test
    public void canSkipToIndex() throws Exception {
        ListZipper<String> zipper = zipper(list("A", "B", "C", "D"));
        assertThat(zipper.index(3).value(), is("D"));
        assertThat(zipper.index(0).value(), is("A"));
        assertThat(zipper.index(2).value(), is("C"));
        assertThat(zipper.index(1).value(), is("B"));
    }


}