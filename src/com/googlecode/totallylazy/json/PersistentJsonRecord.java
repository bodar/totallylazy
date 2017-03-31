package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Pair;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.collections.PersistentSortedMap;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.proxy.Proxy.proxy;

public interface PersistentJsonRecord extends PersistentMap<String, Object> {
    static <T extends PersistentJsonRecord> T create(Class<T> aClass, Map<String, Object> map) {
        Sequence<Method> methods = sequence(aClass.getDeclaredMethods());
        PersistentSortedMap<String, Object> result = methods.fold(sortedMap(), (p, m) -> {
            String name = m.getName();
            Object value = map.remove(name);
            return p.insert(name, Coercer.coerce(m.getGenericReturnType(), value));
        });
        return proxy(aClass, new Handler<>(aClass, pairs(map).fold(result, PersistentSortedMap::cons)));
    }

    static <T extends PersistentJsonRecord> T parse(Class<T> aClass, String json) {
        return create(aClass, Json.map(json));
    }

    <T extends PersistentJsonRecord, R> T modify(Function1<? super T, R> key, R value);


    class Handler<T> implements InvocationHandler {
        private final Class<T> aClass;
        private final PersistentSortedMap<String, Object> map;

        Handler(Class<T> aClass, PersistentSortedMap<String, Object> map) {
            this.aClass = aClass;
            this.map = map;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            String name = method.getName();
            if (name.equals("modify") && (args != null && args.length == 2)) {
                Function1<? super T, ?> key = cast(args[0]);
                AtomicReference<String> n = new AtomicReference<>();
                key.apply(proxy(aClass, (p, m, a) -> {
                    n.set(m.getName());
                    return null;
                }));
                return proxy(aClass, new Handler<T>(aClass, this.map.insert(n.get(), args[1])));
            }
            if (name.equals("toString") && (args == null || args.length == 0)) {
                return Json.json(map);
            }
            if (method.getDeclaringClass().equals(aClass)) {
                return map.get(name);
            }
            return Methods.invoke(method, map, args);
        }
    }
}
