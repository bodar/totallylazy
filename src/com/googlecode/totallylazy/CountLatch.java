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

    public void waitFor(int expectedCount) throws InterruptedException {
        sync.acquireSharedInterruptibly(expectedCount);
    }

    public boolean waitFor(int expectedCount, long timeout, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(expectedCount, unit.toNanos(timeout));
    }

    public void await() throws InterruptedException {
        waitFor(0);
    }

    public boolean await(long timeout, TimeUnit unit) throws InterruptedException {
        return waitFor(0, timeout, unit);
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

    public <A> Function<A> monitor(Callable<? extends A> callable) {
        return monitor(callable, this);
    }

    public static <A> Function<A> monitor(final Callable<? extends A> callable, final CountLatch latch) {
        return new Function<A>() {
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

    public <A, B> Function1<A, B> monitor(Callable1<? super A, ? extends B> callable) {
        return monitor(this, callable);
    }

    public static <A, B> Function1<A, B> monitor(final CountLatch latch, final Callable1<? super A, ? extends B> callable) {
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

        int count() {
            return getState();
        }

        protected int tryAcquireShared(int expected) {
            return (count() == expected) ? 1 : -1;
        }

        protected boolean tryReleaseShared(int adjust) {
            while (true) {
                int oldValue = count();
                int newValue = oldValue + adjust;
                if (compareAndSetState(oldValue, newValue)) return true;
            }
        }
    }
}
