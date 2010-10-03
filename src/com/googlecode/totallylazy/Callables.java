package com.googlecode.totallylazy;

import com.googlecode.totallylazy.callables.LazyCallable;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

import static com.googlecode.totallylazy.Sequences.sequence;
import static java.util.Arrays.asList;

public class Callables {
    public static <T> Callable1<Future<T>, T> realise(){
        return new Callable1<Future<T>, T>() {
            public T call(Future<T> future) throws Exception {
                return future.get();
            }
        };
    }

    public static <T> Callable1<First<T>, T> first() {
        return new Callable1<First<T>, T>() {
            public T call(First<T> first) throws Exception {
                return first.first();
            }
        };
    }

    public static <T> Callable1<Second<T>, T> second() {
        return new Callable1<Second<T>, T>() {
            public T call(Second<T> second) throws Exception {
                return second.second();
            }
        };
    }

    public static <T> Callable1<Iterable<T>, T> head() {
        return new Callable1<Iterable<T>, T>() {
            public T call(Iterable<T> sequence) throws Exception {
                return Sequences.head(sequence);
            }
        };
    }

    public static <T> Callable1<T, String> asString() {
        return new Callable1<T, String>() {
            public String call(T value) {
                return value.toString();
            }
        };
    }

    public static <T> Callable1<Iterable<T>, Iterator<T>> asIterator() {
        return new Callable1<Iterable<T>, Iterator<T>>() {
            public Iterator<T> call(Iterable<T> iterable) throws Exception {
                return iterable.iterator();
            }
        };
    }

    public static <T> Callable1<Iterator<T>, Iterable<T>> asIterable() {
        return new Callable1<Iterator<T>, Iterable<T>>() {
            public Iterable<T> call(final Iterator<T> iterator) throws Exception {
                return Sequences.sequence(iterator);
            }
        };
    }



    public static <T> Callable<T> returns(final T t) {
        return new Callable<T>() {
            public T call() throws Exception {
                return t;
            }
        };
    }

    public static <T> Callable1<T, T> returnArgument() {
        return new Callable1<T, T>() {
            public T call(T value) {
                return value;
            }
        };
    }

    public static <T> Callable1<T, T> returnArgument(Class<T> aClass) {
        return returnArgument();
    }

    public static <T> Callable<T> aNull(Class<T> aClass) {
        return new Callable<T>() {
            public T call() throws Exception {
                return null;
            }
        };
    }

    public static <T> Callable<T> callThrows(final Exception e) {
        return new Callable<T>() {
            public T call() throws Exception {
                throw e;
            }
        };
    }

    public static <T> Callable<T> callThrows(final Exception e, Class<T> aClass) {
        return callThrows(e);
    }

    public static <T> Callable1<Callable<T>, T> call() {
        return new Callable1<Callable<T>, T>() {
            public T call(Callable<T> callable) throws Exception {
                return callable.call();
            }
        };
    }

    public static <T> Callable1<Callable<T>, T> call(Class<T> aClass) {
        return call();
    }

    public static Callable1<Integer, Integer> increment() {
        return add(1);
    }

    public static Callable1<Integer, Integer> add(final int amount) {
        return new Callable1<Integer, Integer>() {
            public Integer call(final Integer integer) throws Exception {
                return integer + amount;
            }
        };
    }

    public static Callable2<Integer, Integer, Integer> add() {
        return new Callable2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        };
    }

}
