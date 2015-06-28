package com.googlecode.totallylazy.proxy;

import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import static com.googlecode.totallylazy.Assert.assertThat;
import static com.googlecode.totallylazy.predicates.Predicates.is;

public class ProxyBuilderTest {
    @Test
    public void supportsCreatingAClass() throws Throwable {
        SubClassMe instance = ProxyBuilder.proxy(SubClassMe.class, (proxy, method, args) -> {
            System.out.println("method = " + method);
            return 12;
        });

        assertThat(instance.add(1,2), is(12));
    }

}