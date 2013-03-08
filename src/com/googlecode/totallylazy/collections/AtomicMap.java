package com.googlecode.totallylazy.collections;

import com.googlecode.totallylazy.Atomic;
import com.googlecode.totallylazy.Mapper;
import com.googlecode.totallylazy.Maps;
import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Segment;
import com.googlecode.totallylazy.UnaryFunction;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import static com.googlecode.totallylazy.Atomic.constructors.atomic;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;

public class AtomicMap<K, V> implements Map<K, V> {
    private final Atomic<PersistentMap<K, V>> atomic;

    private AtomicMap(Atomic<PersistentMap<K, V>> atomic) {
        this.atomic = atomic;
    }

    public static <K, V> AtomicMap<K, V> atomicMap(PersistentMap<K, V> map) {return new AtomicMap<K, V>(atomic(map));}

    private PersistentMap<K, V> map() {return atomic.value();}

    private K key(Object key) {return cast(key);}

    @Override
    public int size() {
        return map().size();
    }

    @Override
    public boolean isEmpty() {
        return map().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map().contains(key(key));
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public V get(Object key) {
        return map().get(key(key)).getOrNull();
    }

    @Override
    public V put(final K key, final V value) {
        return atomic.modifyReturn(new Mapper<PersistentMap<K, V>, Pair<PersistentMap<K, V>, V>>() {
            @Override
            public Pair<PersistentMap<K, V>, V> call(PersistentMap<K, V> map) throws Exception {
                return PersistentMap.methods.put(map, key, value).
                        second(Option.functions.<V>getOrElse(null));
            }
        });
    }

    @Override
    public V remove(final Object key) {
        return atomic.modifyReturn(new Mapper<PersistentMap<K, V>, Pair<PersistentMap<K, V>, V>>() {
            @Override
            public Pair<PersistentMap<K, V>, V> call(PersistentMap<K, V> map) throws Exception {
                return PersistentMap.methods.remove(map, key(key)).
                        second(Option.functions.<V>getOrElse(null));
            }
        });
    }

    @Override
    public void putAll(final Map<? extends K, ? extends V> m) {
        atomic.modify(new UnaryFunction<PersistentMap<K, V>>() {
            @Override
            public PersistentMap<K, V> call(PersistentMap<K, V> map) throws Exception {
                return Maps.pairs(m).<Pair<K, V>>unsafeCast().fold(map, Segment.functions.<Pair<K, V>, PersistentMap<K, V>>cons());
            }
        });
    }

    @Override
    public void clear() {
        atomic.modify(new UnaryFunction<PersistentMap<K, V>>() {
            @Override
            public PersistentMap<K, V> call(PersistentMap<K, V> map) throws Exception {
                return map.empty();
            }
        });
    }

    @Override
    public Set<K> keySet() {
        return map().keys().toSet();
    }

    @Override
    public Collection<V> values() {
        return map().values().toList();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return Maps.entrySet(map());
    }
}
