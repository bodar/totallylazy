package com.googlecode.totallylazy;

import com.googlecode.totallylazy.functions.Function1;

import static com.googlecode.totallylazy.Option.option;
import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Debug {
    private static final String TRACE_PROPERTY = "com.googlecode.totallylazy.trace";

    public static boolean inDebug() {
        return debugging();
    }

    public static boolean debugging() {
        return getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    public static void trace(Exception e) {
        if (debugging() && traceEnabled()) {
            e.printStackTrace();
        }
    }

    public static class functions {
        public static <A,B> Function1<A,B> trace(final Function1<? super A,? extends B> callable) {
            return new Function1<A, B>() {
                @Override
                public B call(A a) throws Exception {
                    try {
                        return callable.call(a);
                    } catch (Exception e) {
                        Debug.trace(e);
                        throw e;
                    }
                }
            };
        }
    }

    private static boolean traceEnabled() {
        return option(System.getProperty(TRACE_PROPERTY))
                .map(Boolean::parseBoolean)
                .getOrElse(true);
    }
}