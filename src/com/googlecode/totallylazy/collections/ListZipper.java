package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Callable1;
import com.googlecode.totallylazy.Functions;

import static com.googlecode.totallylazy.collections.ImmutableList.functions;

public class ListZipper<T> {
    public final ImmutableList<T> focus;
    public final ImmutableList<T> breadcrumbs;

    private ListZipper(ImmutableList<T> focus, ImmutableList<T> breadcrumbs) {
        this.focus = focus;
        this.breadcrumbs = breadcrumbs;
    }

    public static <T> ListZipper<T> zipper(ImmutableList<T> focus) {
        return zipper(focus, ImmutableList.constructors.<T>empty());
    }

    private static <T> ListZipper<T> zipper(ImmutableList<T> focus, ImmutableList<T> breadcrumbs) {
        return new ListZipper<T>(focus, breadcrumbs);
    }

    public ListZipper<T> right() {
        return zipper(focus.tail(), breadcrumbs.cons(focus.head()));
    }

    public ListZipper<T> left() {
        return zipper(focus.cons(breadcrumbs.head()), breadcrumbs.tail());
    }

    public ListZipper<T> top() {
        return top(this);
    }

    public static <T> ListZipper<T> top(ListZipper<T> zipper) {
        ImmutableList<T> focus = zipper.focus;
        ImmutableList<T> breadcrumbs = zipper.breadcrumbs;
        while (!breadcrumbs.isEmpty()) {
            focus = focus.cons(breadcrumbs.head());
            breadcrumbs = breadcrumbs.tail();
        }
        return zipper(focus, breadcrumbs);
    }

    @Override
    public int hashCode() {
        return focus.hashCode() * breadcrumbs.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ListZipper && (((ListZipper) other).focus.equals(focus) && ((ListZipper) other).breadcrumbs.equals(breadcrumbs));
    }

    @Override
    public String toString() {
        return String.format("focus(%s), breadcrumbs(%s)", focus, breadcrumbs);
    }

    public ListZipper<T> modify(Callable1<? super ImmutableList<T>, ? extends ImmutableList<T>> callable) {
        return zipper(Functions.call(callable, focus), breadcrumbs);
    }

    public ImmutableList<T> toList() {
        return top().focus;
    }

    public ListZipper<T> insert(T instance) {
        return modify(functions.cons(instance));
    }

    public ListZipper<T> remove() {
        return delete();
    }

    public ListZipper<T> delete() {
        return modify(functions.<T>tail());
    }

    public T current() {
        return focus.head();
    }

    public boolean isBottom() {
        return focus.isEmpty();
    }

    public boolean isTop() {
        return breadcrumbs.isEmpty();
    }
}