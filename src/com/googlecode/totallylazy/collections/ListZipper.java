package com.googlecode.totallylazy.collections;

public class ListZipper<T> {
    private final ImmutableList<T> focus;
    private final ImmutableList<T> breadcrumbs;

    private ListZipper(ImmutableList<T> focus, ImmutableList<T> breadcrumbs) {
        this.focus = focus;
        this.breadcrumbs = breadcrumbs;
    }

    public static <T> ListZipper<T> listZipper(ImmutableList<T> focus, ImmutableList<T> breadcrumbs) {
        return new ListZipper<T>(focus, breadcrumbs);
    }

    public static <T> ListZipper<T> listZipper(ImmutableList<T> focus) {
        return new ListZipper<T>(focus, ImmutableList.constructors.<T>empty());
    }

    public ListZipper<T> forward() {
        return listZipper(focus.tail(), breadcrumbs.cons(focus.head()));
    }

    public ListZipper<T> backward() {
        return listZipper(focus.cons(breadcrumbs.head()), breadcrumbs.tail());
    }

    public ImmutableList<T> focus() {
        return focus;
    }

    public ImmutableList<T> breadcrumbs() {
        return breadcrumbs;
    }

    public static <T> ListZipper<T> toStart(ListZipper<T> zipper) {
        while(!zipper.breadcrumbs().isEmpty()) zipper = zipper.backward();
        return zipper;
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
}