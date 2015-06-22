package com.googlecode.totallylazy.proxy;

import net.sf.cglib.proxy.InvocationHandler;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static com.googlecode.totallylazy.Sequences.repeat;
import static com.googlecode.totallylazy.proxy.Proxy.createProxy;

public class ProxyTest {
    @Test
    @Ignore("Manual test")
    public void doesNotEatPermGen() {
        for (User user : repeat((Callable<User>) () -> {
            createProxy(User.class, (o, method, objects) -> null);
            return null;
        })) {
        }
    }
}
