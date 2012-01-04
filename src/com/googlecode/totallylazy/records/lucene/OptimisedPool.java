package com.googlecode.totallylazy.records.lucene;

import com.googlecode.totallylazy.Function;
import com.googlecode.totallylazy.Function1;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.googlecode.totallylazy.Predicates.is;
import static com.googlecode.totallylazy.Predicates.not;
import static com.googlecode.totallylazy.Predicates.where;
import static com.googlecode.totallylazy.Runnables.VOID;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.records.lucene.OptimisedPool.PooledValue.checkoutCount;
import static com.googlecode.totallylazy.records.lucene.OptimisedPool.PooledValue.checkoutValue;
import static com.googlecode.totallylazy.records.lucene.OptimisedPool.PooledValue.dirty;

public class OptimisedPool implements SearcherPool {
    private final List<PooledValue> pool = new CopyOnWriteArrayList<PooledValue>();
    private final Directory directory;

    public OptimisedPool(Directory directory) {
        this.directory = directory;
    }

    @Override
    public int size() {
        return pool.size();
    }

    @Override
    public synchronized Searcher searcher() throws IOException {
        return sequence(pool).find(where(dirty(), is(false))).
                map(checkoutValue()).
                getOrElse(createSearcher());
    }

    private Function<Searcher> createSearcher() throws IOException {
        return new Function<Searcher>() {
            @Override
            public Searcher call() throws Exception {
                PooledSearcher pooledSearcher = new PooledSearcher(new IndexSearcher(directory), checkIn());
                pool.add(new PooledValue(pooledSearcher));
                return pooledSearcher;
            }
        };
    }

    private Function1<Searcher, Void> checkIn() {
        return new Function1<Searcher, Void>() {
            @Override
            public Void call(Searcher searcher) throws Exception {
                checkin(searcher);
                return VOID;
            }
        };
    }

    private synchronized void checkin(Searcher searcher) throws IOException {
        PooledValue pooledValue = sequence(pool).find(where(PooledValue.searcher(), is(searcher))).get();
        int count = pooledValue.checkin();
        if(count == 0 && pooledValue.dirty){
            closeAndRemove(pooledValue);
        }
    }

    @Override
    public synchronized void markAsDirty() {
        sequence(pool).filter(where(checkoutCount(), is(0))).
                each(closeAndRemove());
        sequence(pool).filter(where(checkoutCount(), is(not(0)))).
                each(PooledValue.markAsDirty());

    }

    private Function1<PooledValue, Void> closeAndRemove() {
        return new Function1<PooledValue, Void>() {
            @Override
            public Void call(PooledValue pooledValue) throws Exception {
                closeAndRemove(pooledValue);
                return VOID;
            }
        };
    }

    private void closeAndRemove(PooledValue pooledValue) throws IOException {
        // TODO clean me up
        pooledValue.searcher.searcher.close();
        pool.remove(pooledValue);
    }

    @Override
    public void close() throws IOException {
        // TODO clean me up
        for (PooledValue pooledValue : pool) {
            pooledValue.searcher.searcher.close();
        }
    }

    static class PooledValue {
        private final PooledSearcher searcher;
        private boolean dirty = false;
        private int checkoutCount = 1;

        private PooledValue(PooledSearcher searcher) {
            this.searcher = searcher;
        }

        public static Function1<PooledValue, Boolean> dirty() {
            return new Function1<PooledValue, Boolean>() {
                @Override
                public Boolean call(PooledValue value) {
                    return value.dirty;
                }
            };
        }

        public void dirty(boolean value) {
            this.dirty = value;
        }

        public static Function1<PooledValue, Searcher> checkoutValue() {
            return new Function1<PooledValue, Searcher>() {
                @Override
                public Searcher call(PooledValue pooledValue) throws Exception {
                    return pooledValue.checkout();
                }
            };
        }

        public static Function1<PooledValue, Searcher> searcher() {
            return new Function1<PooledValue, Searcher>() {
                @Override
                public Searcher call(PooledValue pooledValue) throws Exception {
                    return pooledValue.searcher;
                }
            };
        }

        private Searcher checkout() {
            checkoutCount++;
            return searcher;
        }

        public static Function1<PooledValue, Integer> checkoutCount() {
            return new Function1<PooledValue, Integer>() {
                @Override
                public Integer call(PooledValue pooledValue) throws Exception {
                    return pooledValue.checkoutCount;
                }
            };
        }

        public int checkin() {
            return --checkoutCount;
        }

        public static Function1<PooledValue, Void> markAsDirty() {
            return new Function1<PooledValue, Void>() {
                @Override
                public Void call(PooledValue pooledValue) throws Exception {
                    pooledValue.dirty(true);
                    return VOID;
                }
            };
        }
    }
}
