package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.functions.Functions;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.annotations.tailrec;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.collections.PersistentList.functions;

public class ListZipper<T> implements Zipper<T> {
    public final PersistentList<T> focus;
    public final PersistentList<T> breadcrumbs;

    private ListZipper(PersistentList<T> focus, PersistentList<T> breadcrumbs) {
        this.focus = focus;
        this.breadcrumbs = breadcrumbs;
    }

    public static <T> ListZipper<T> zipper(PersistentList<T> focus) {
        return zipper(focus, PersistentList.constructors.<T>empty());
    }

    private static <T> ListZipper<T> zipper(PersistentList<T> focus, PersistentList<T> breadcrumbs) {
        return new ListZipper<T>(focus, breadcrumbs);
    }

    public Option<ListZipper<T>> nextOption() {
        if(isLast()) return none();
        return focus.headOption().map(t -> zipper(focus.tail(), breadcrumbs.cons(t)));
    }

    public Option<ListZipper<T>> previousOption() {
        return breadcrumbs.headOption().map(t -> zipper(focus.cons(t), breadcrumbs.tail()));
    }

    @Override
    public ListZipper<T> next() {
        return nextOption().get();
    }

    @Override
    public ListZipper<T> previous() {
        return previousOption().get();
    }

    @Override
    public ListZipper<T> first() {
        return top();
    }

    @Override
    public ListZipper<T> last() {
        return bottom();
    }

    @Override
    public boolean isFirst() {
        return isTop();
    }

    @Override
    public boolean isLast() {
        return isBottom();
    }

    @tailrec
    public ListZipper<T> top() {
        if (isTop()) return this;
        return previous().top();
    }

    @tailrec
    public ListZipper<T> bottom() {
        if (isBottom()) return this;
        return next().bottom();
    }

    @Override
    public T value() {
        return focus.head();
    }

    @Override
    public int index() {
        return breadcrumbs.size();
    }

    @Override
    public ListZipper<T> index(int index) {
        int position = index();
        if (position == index) return this;
        if (position < index) return next().index(index);
        return previous().index(index);
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

    public ListZipper<T> modify(Function1<? super PersistentList<T>, ? extends PersistentList<T>> callable) {
        return zipper(Functions.call(callable, focus), breadcrumbs);
    }

    public PersistentList<T> toList() {
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
        return focus.tail().isEmpty();
    }

    public boolean isTop() {
        return breadcrumbs.isEmpty();
    }
}