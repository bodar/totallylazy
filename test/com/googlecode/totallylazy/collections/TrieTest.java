package com.googlecode.totallylazy.collections;

import org.junit.Test;

import static com.googlecode.totallylazy.collections.ImmutableSortedMap.constructors;
import static com.googlecode.totallylazy.collections.Trie.trie;
import static com.googlecode.totallylazy.matchers.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class TrieTest {
    @Test
    public void get() throws Exception {
        Trie<String> trie = trie(null, constructors.<Character, Trie<String>>sortedMap('a', trie(null, constructors.<Character, Trie<String>>sortedMap('b', trie("Foo")))));
        assertThat(trie.get("ab"), is("Foo"));
    }

    @Test
    public void put() throws Exception {
        Trie<String> trie = Trie.<String>trie().put("ab", "Foo");
        assertThat(trie.get("ab"), is("Foo"));
    }
}
