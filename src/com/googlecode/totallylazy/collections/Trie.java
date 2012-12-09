package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.Value;

import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;

public class Trie<K, V> implements Value<V> {
    private final Option<V> value;
    private final ImmutableMap<K, Trie<K, V>> children;

    private Trie(Option<V> value, ImmutableMap<K, Trie<K, V>> children) {
        this.value = value;
        this.children = children;
    }

    public static <K, V> Trie<K, V> trie() {
        return trie(Option.<V>none());
    }

    public static <K, V> Trie<K, V> trie(Option<V> value) {
        return trie(value, ListMap.<K, Trie<K, V>>emptyListMap());
    }

    public static <K, V> Trie<K, V> trie(Option<V> value, ImmutableMap<K, Trie<K, V>> children) {
        return new Trie<K, V>(value, children);
    }

    public boolean contains(Segment<K> key){
        if(key.isEmpty()) return !value.isEmpty();
        Option<Trie<K, V>> child = childFor(key);
        return !child.isEmpty() && child.get().contains(key.tail());
    }

    public Option<V> get(Segment<K> key) {
        if(key.isEmpty()) return value;
        Option<Trie<K, V>> child = childFor(key);
        if(child.isEmpty()) return none();
        return child.get().get(key.tail());
    }

    public Trie<K, V> put(Segment<K> key, V value) {
        if(key.isEmpty()) return trie(option(value), children);
        return trie(this.value, children.put(key.head(), childFor(key).getOrElse(Trie.<K, V>trie()).put(key.tail(), value)));
    }

    public Trie<K, V> remove(Segment<K> key) {
        return put(key, null);
    }

    public V value() {
        return value.get();
    }

    public boolean isEmpty(){
        return value.isEmpty() && children.isEmpty();
    }

    private Option<Trie<K, V>> childFor(Segment<K> key) {return children.get(key.head());}
}
