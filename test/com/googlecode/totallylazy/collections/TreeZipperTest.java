package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Sequences;
import com.googlecode.totallylazy.comparators.Comparators;
import org.junit.Test;

import java.util.Comparator;

import static com.googlecode.totallylazy.Pair.pair;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.collections.AVLTree.constructors.avlTree;
import static com.googlecode.totallylazy.collections.TreeMap.constructors.factory;
import static com.googlecode.totallylazy.collections.TreeZipper.Breadcrumb.breadcrumb;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.left;
import static com.googlecode.totallylazy.collections.TreeZipper.Direction.right;
import static com.googlecode.totallylazy.collections.TreeZipper.functions.replace;
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
        assertThat(newZipper.breadcrumbs, is(sequence(
                breadcrumb(left, pair(1, 1), avlTree(2, 2)),
                breadcrumb(left, pair(3, 3), avlTree(4, 4).put(5, 5).put(6, 6)))));
    }

    @Test
    public void canGoRight() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.right().right();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(6, 6)));
        assertThat(newZipper.breadcrumbs, is(sequence(
                breadcrumb(right, pair(5, 5), avlTree(4, 4)),
                breadcrumb(right, pair(3, 3), avlTree(0, 0).put(1, 1).put(2, 2)))));
    }

    @Test
    public void canGoUpFromRight() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.right().right().up();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(4, 4).put(5, 5).put(6, 6)));
        assertThat(newZipper.breadcrumbs, is(sequence(
                breadcrumb(right, pair(3, 3), avlTree(0, 0).put(1, 1).put(2, 2)))));
    }

    @Test
    public void canGoUpFromLeft() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.left().left().up();
        assertThat(newZipper.focus, is((TreeMap<Integer, Integer>) avlTree(0, 0).put(1, 1).put(2, 2)));
        assertThat(newZipper.breadcrumbs, is(sequence(
                breadcrumb(left, pair(3, 3), avlTree(4, 4).put(5, 5).put(6, 6)))));
    }

    @Test
    public void canGoToTop() {
        final TreeZipper<Integer, Integer> zipper =
                zipper(avlTree(0, 0).put(1, 1).put(2, 2).put(3, 3).put(4, 4).put(5, 5).put(6, 6));

        final TreeZipper<Integer, Integer> newZipper = zipper.left().left().toStart();
        assertThat(newZipper.focus, is(zipper.focus));
        assertThat(newZipper.breadcrumbs, is(Sequences.<TreeZipper.Breadcrumb<Integer, Integer>>empty()));
    }

    @Test
    public void canReplaceElementInMiddleOfTree() {
        final Comparator<Integer> comparator = Comparators.<Integer>ascending();
        TreeMap<Integer, Integer> treeMap =
                factory.create(comparator, 0, 0,
                        factory.create(comparator, 2, 2,
                                factory.create(comparator, 3, 3), factory.<Integer, Integer>create(comparator)),
                        factory.create(comparator, 4, 4));
        final TreeMap<Integer, Integer> newTreeMap = zipper(treeMap).left().modify(replace(9, 9)).toTreeMap();
        final TreeMap<Integer, Integer> expected =
                factory.create(comparator, 0, 0,
                        factory.create(comparator, 9, 9,
                                factory.create(comparator, 3, 3), factory.<Integer, Integer>create(comparator)),
                        factory.create(comparator, 4, 4));
        assertThat(newTreeMap, is(expected));
        assertThat(zipper(treeMap).left().replace(9, 9).toTreeMap(), is(expected));
    }
}