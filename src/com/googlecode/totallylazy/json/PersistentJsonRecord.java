package com.googlecode.totallylazy.json;

import com.googlecode.totallylazy.Option;
import com.googlecode.totallylazy.Sequence;
import com.googlecode.totallylazy.collections.PersistentMap;
import com.googlecode.totallylazy.collections.PersistentSortedMap;
import com.googlecode.totallylazy.functions.Function1;
import com.googlecode.totallylazy.reflection.Fields;
import com.googlecode.totallylazy.reflection.Methods;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.googlecode.totallylazy.Maps.pairs;
import static com.googlecode.totallylazy.Sequences.sequence;
import static com.googlecode.totallylazy.Unchecked.cast;
import static com.googlecode.totallylazy.collections.PersistentSortedMap.constructors.sortedMap;
import static com.googlecode.totallylazy.proxy.Proxy.isProxy;
import static com.googlecode.totallylazy.proxy.Proxy.proxy;
import static com.googlecode.totallylazy.reflection.Fields.fields;

public interface PersistentJsonRecord extends PersistentMap<String, Object> {
    static <T> T create(Class<T> aClass, Map<String, Object> map) {
        // TODO: Check is interface
        Sequence<Method> methods = sequence(aClass.getDeclaredMethods());
        PersistentSortedMap<String, Object> result = methods.fold(sortedMap(), (p, m) -> {
            String name = m.getName();
            Object value = map.remove(name);
            return value == null ? p : p.insert(name, Coercer.coerce(m.getGenericReturnType(), value));
        });
        return proxy(aClass, new Handler<>(aClass, pairs(map).fold(result, PersistentSortedMap::cons)));
    }

    static <T> T parse(Class<T> aClass, String json) {
        return create(aClass, Json.map(json));
    }

    static <T, R> T modify(T instance, Function1<? super T, R> key, R value){
        // TODO: Find interface correctly
        Class<T> aClass = cast(instance.getClass().getInterfaces()[0]);
        AtomicReference<String> n = new AtomicReference<>();
        key.apply(proxy(aClass, (p, m, a) -> {
            n.set(m.getName());
            return null;
        }));
        return proxy(aClass, new Handler<T>(aClass, map(instance).insert(n.get(), value)));
    }

    static PersistentMap<String, Object> map(Object instance) {
        if(isProxy(instance)){
            Option<PersistentMap<String, Object>> map = fields(instance.getClass()).
                    filter(f -> f.getType().equals(InvocationHandler.class)).
                    map(f -> Fields.get(f, instance)).
                    safeCast(Handler.class).
                    map(h -> h.map).
                    <PersistentMap<String, Object>>unsafeCast().
                    headOption();
            if(map.isDefined()) return map.get();
        }
        // TODO: Find interface correctly
        Sequence<Method> methods = sequence(instance.getClass().getInterfaces()[0].getDeclaredMethods());
        return methods.fold(sortedMap(), (p, m) -> {
            String name = m.getName();
            Object value = Methods.invoke(m, instance);
            return value == null ? p : p.insert(name, value);
        });
    }

    class Handler<T> implements InvocationHandler {
        final Class<T> aClass;
        final PersistentMap<String, Object> map;

        Handler(Class<T> aClass, PersistentMap<String, Object> map) {
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
