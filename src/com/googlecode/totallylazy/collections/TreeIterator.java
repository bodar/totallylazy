package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.iterators.StatefulIterator;

import static com.googlecode.totallylazy.Option.some;
import static com.googlecode.totallylazy.collections.TreeZipper.zipper;

public class TreeIterator<K, V> extends StatefulIterator<Pair<K, V>> {
    private Option<TreeZipper<K, V>> zipper;

    public TreeIterator(TreeMap<K, V> treeMap) {
        this.zipper = some(zipper(treeMap).first());
    }

    @Override
    protected Pair<K, V> getNext() throws Exception {
        if(zipper.isEmpty()) return finished();
        Pair<K, V> pair = zipper.get().pair();
        zipper = zipper.get().nextOption();
        return pair;
    }
}
