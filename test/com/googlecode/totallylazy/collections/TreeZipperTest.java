package com.googlecode.totallylazy.collections;

import org.junit.Test;

import java.util.NoSuchElementException;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.ImmutableList.constructors.list;
import static com.googlecode.totallylazy.collections.TreeMap.functions.replace;
import static com.googlecode.totallylazy.collections.TreeZipper.Breadcrumb.breadcrumb;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.left;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.right;
import static com.googlecode.totallylazy.collections.TreeZipper.zipper;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TreeZipperTest {
    @Test
    public void canGoLeft() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.left().left();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(0, 0)));
        assertThat(newZipper.breadcrumbs, is(list(
                breadcrumb(left, pair(1, 1), avlTree(2, 2)),
                breadcrumb(left, pair(3, 3), avlTree(4, 4).put(5, 5).put(6, 6)))));
    }

    @Test
    public void canGoRight() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.right().right();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(6, 6)));
        assertThat(newZipper.breadcrumbs, is(list(
                breadcrumb(right, pair(5, 5), avlTree(4, 4)),
                breadcrumb(right, pair(3, 3), avlTree(0, 0).put(1, 1).put(2, 2)))));
    }

    @Test
    public void canGoUpFromRight() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.right().right().up();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(4, 4).put(5, 5).put(6, 6)));
        assertThat(newZipper.breadcrumbs, is(list(
                breadcrumb(right, pair(3, 3), avlTree(0, 0).put(1, 1).put(2, 2)))));
    }

    @Test
    public void canGoUpFromLeft() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.left().left().up();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(0, 0).put(1, 1).put(2, 2)));
        assertThat(newZipper.breadcrumbs, is(list(
                breadcrumb(left, pair(3, 3), avlTree(4, 4).put(5, 5).put(6, 6)))));
    }

    @Test
    public void canGoToTop() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.left().left().toStart();
        assertThat(newZipper.focus, is(zipper.focus));
        assertThat(newZipper.breadcrumbs, is(ImmutableList.constructors.<TreeZipper.Breadcrumb<Integer, Integer>>empty()));
    }

    @Test
    public void canReplaceElementInMiddleOfTree() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(10, 10).put(20, 20));
        final TreeZipper<Integer, Integer> newZipper = zipper.left().modify(replace(5, 5));
        assertThat(newZipper.toTreeMap(), is((TreeMap<Integer, Integer>) avlTree(5, 5).put(10, 10).put(20, 20)));
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(5, 5)));

        assertThat(zipper.left().replace(5, 5).toTreeMap(), is((TreeMap<Integer, Integer>) avlTree(5, 5).put(10, 10).put(20, 20)));
    }

    @Test
    public void canPerformMultipleModifications() throws Exception {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));
        final TreeZipper<Integer, Integer> newZipper = zipper.left().left().delete().delete();
        assertThat(newZipper.toTreeMap(), is((TreeMap<Integer, Integer>) avlTree(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6)));
    }

    @Test
    public void canDelete() throws Exception {
        assertThat(zipper(avlTree(0, 0).put(10, 10).put(20, 20)).left().delete().toTreeMap(),
                is((TreeMap<Integer, Integer>) avlTree(10, 10).put(20, 20)));
    }

    @Test
    public void canDeleteRootNode() throws Exception {
        assertThat(zipper(avlTree(0, 0).put(10, 10).put(20, 20)).delete().toTreeMap(),
                is((TreeMap<Integer, Integer>) avlTree(0, 0).put(20, 20)));
    }

    @Test(expected = NoSuchElementException.class)
    public void throwsOnDeletingEmptyNode() throws Exception {
        zipper(avlTree(0, 0)).delete().delete().toTreeMap();
    }
}