package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Value;

import static com.googlecode.totallylazy.Option.some;

public class Trie<V> implements Value<V> {
    private final Option<V> value;
    private final ImmutableSortedMap<Character, Trie<V>> children;

    private Trie(Option<V> value, ImmutableSortedMap<Character, Trie<V>> children) {
        this.value = value;
        this.children = children;
    }

    public static <T> Trie<T> trie() {
        return trie(null);
    }

    public static <T> Trie<T> trie(Option<T> value) {
        return trie(value, ImmutableSortedMap.constructors.<Character, Trie<T>>emptySortedMap());
    }

    public static <T> Trie<T> trie(Option<T> value, ImmutableSortedMap<Character, Trie<T>> children) {
        return new Trie<T>(value, children);
    }

    public Option<V> get(CharSequence key) {
        if(key.length() == 0) return value;
        return childFor(key).get().get(tail(key));
    }

    public Trie<V> put(CharSequence key, V value) {
        if(key.length() == 0) return trie(some(value), children);
        return trie(this.value, children.put(key.charAt(0), childFor(key).getOrElse(Trie.<V>trie()).put(tail(key), value)));
    }

    public V value() {
        return value.get();
    }

    private Option<Trie<V>> childFor(CharSequence key) {return children.get(key.charAt(0));}

    private CharSequence tail(CharSequence key) {return key.subSequence(1, key.length());}
}
