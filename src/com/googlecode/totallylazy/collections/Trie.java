package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Value;

public class Trie<T> implements Value<T> {
    private final T value;
    private final ImmutableSortedMap<Character, Trie<T>> children;

    private Trie(T value, ImmutableSortedMap<Character, Trie<T>> children) {
        this.value = value;
        this.children = children;
    }

    public static <T> Trie<T> trie() {
        return trie(null);
    }

    public static <T> Trie<T> trie(T value) {
        return trie(value, ImmutableSortedMap.constructors.<Character, Trie<T>>emptySortedMap());
    }

    public static <T> Trie<T> trie(T value, ImmutableSortedMap<Character, Trie<T>> children) {
        return new Trie<T>(value, children);
    }

    public ImmutableSortedMap<Character, Trie<T>> children() {
        return children;
    }

    public T get(CharSequence key) {
        if(key.length() == 0) return value();
        return childFor(key).get().get(tail(key));
    }

    public Trie<T> put(CharSequence key, T value) {
        if(key.length() == 0) return trie(value, children);
        return trie(value(), children.put(key.charAt(0), childFor(key).getOrElse(Trie.<T>trie()).put(tail(key), value)));
    }

    public T value() {
        return value;
    }

    private Option<Trie<T>> childFor(CharSequence key) {return children.get(key.charAt(0));}

    private CharSequence tail(CharSequence key) {return key.subSequence(1, key.length());}
}
