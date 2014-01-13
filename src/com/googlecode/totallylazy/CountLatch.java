package com.googlecode.totallylazy;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class CountLatch {
    private final Sync sync;

    public CountLatch(int count) {
        this.sync = new Sync(count);
    }

    public CountLatch() {
        this(0);
    }

    public void await() throws InterruptedException {
        sync.acquireSharedInterruptibly(-1);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(-1, unit.toNanos(timeout));
    }

    public void countUp() {
        sync.releaseShared(1);
    }

    public void countDown() {
        sync.releaseShared(-1);
    }

    public int count() {
        return sync.count();
    }

    public String toString() {
        return String.format("Latch(%d)", sync.count());
    }

    public <A> Returns<A> monitor(Callable<? extends A> callable) {
        return monitor(callable, this);
    }

    public static <A> Returns<A> monitor(final Callable<? extends A> callable, final CountLatch latch) {
        return new Returns<A>() {
            @Override
            public A call() throws Exception {
                latch.countUp();
                try {
                    return callable.call();
                } finally {
                    latch.countDown();
                }
            }
        };
    }

    public <A, B> Function1<A, B> monitor(Function<? super A, ? extends B> callable) {
        return monitor(this, callable);
    }

    public static <A, B> Function1<A, B> monitor(final CountLatch latch, final Function<? super A, ? extends B> callable) {
        return new Function1<A, B>() {
            @Override
            public B call(A a) throws Exception {
                latch.countUp();
                try {
                    return callable.call(a);
                } finally {
                    latch.countDown();
                }
            }
        };
    }

    private static final class Sync extends AbstractQueuedSynchronizer {
        public Sync(int count) {
            setState(count);
        }

        protected final int tryAcquireShared(int ignore) {
            return finished() ? 1 : -1;
        }

        protected final boolean tryReleaseShared(int adjust) {
            while (true) {
                int oldValue = count();
                int newValue = oldValue + adjust;
                if (compareAndSetState(oldValue, newValue)) return finished();
            }
        }

        final int count() {
            return getState();
        }

        final boolean finished() {
            return count() == 0;
        }
    }
}
