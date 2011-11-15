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
        for (User user : repeat(new Callable<User>() {
            public User call() throws Exception {
                createProxy(User.class, new InvocationHandler() {
                    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                        return null;
                    }
                });
                return null;
            }
        })) {
        }
    }
}
