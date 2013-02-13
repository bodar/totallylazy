package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Arrays;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Value;
import com.googlecode.totallylazy.annotations.tailrec;

import static com.googlecode.totallylazy.Arrays.head;
import static com.googlecode.totallylazy.Arrays.tail;
import static com.googlecode.totallylazy.Option.none;
import static com.googlecode.totallylazy.Option.option;

public class ArrayTrie<K, V> implements Value<V> {
    private final Option<V> value;
    private final PersistentMap<K, ArrayTrie<K, V>> children;

    private ArrayTrie(Option<V> value, PersistentMap<K, ArrayTrie<K, V>> children) {
        this.value = value;
        this.children = children;
    }

    public static <K, V> ArrayTrie<K, V> trie() {
        return trie(Option.<V>none());
    }

    public static <K, V> ArrayTrie<K, V> trie(Option<V> value) {
        return trie(value, ListMap.<K, ArrayTrie<K, V>>emptyListMap());
    }

    public static <K, V> ArrayTrie<K, V> trie(Option<V> value, PersistentMap<K, ArrayTrie<K, V>> children) {
        return new ArrayTrie<K, V>(value, children);
    }

    public boolean contains(K[] key) {
        if (Arrays.isEmpty(key)) return !value.isEmpty();
        Option<ArrayTrie<K, V>> child = childFor(key);
        return !child.isEmpty() && child.get().contains(tail(key));
    }

    @tailrec
    public Option<V> get(K[] key) {
        if (Arrays.isEmpty(key)) return value;
        Option<ArrayTrie<K, V>> child = childFor(key);
        if (child.isEmpty()) return none();
        return child.get().get(tail(key));
    }

    public ArrayTrie<K, V> put(K[] key, V value) {
        if (Arrays.isEmpty(key)) return trie(option(value), children);
        return trie(this.value, children.put(head(key), childFor(key).getOrElse(ArrayTrie.<K, V>trie()).put(tail(key), value)));
    }

    public ArrayTrie<K, V> remove(K[] key) {
        return put(key, null);
    }

    public V value() {
        return value.get();
    }

    public boolean isEmpty() {
        return value.isEmpty() && children.isEmpty();
    }

    private Option<ArrayTrie<K, V>> childFor(K[] key) {return children.get(head(key));}
}
