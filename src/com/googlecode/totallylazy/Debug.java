package com.googlecode.totallylazy;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Debug {
    private static final String TRACE_PROPERTY = "com.googlecode.totallylazy.trace";

    public static boolean inDebug() {
        return debugging();
    }

    public static boolean debugging() {
        return getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    public static void trace(Throwable throwable) {
        if (debugging() && traceEnabled()) {
            throwable.printStackTrace();
        }
    }

    public static class functions {
        public static <A,B> Mapper<A,B> trace(final Callable1<? super A,? extends B> callable) {
            return new Mapper<A, B>() {
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
        String traceProperty = System.getProperty(TRACE_PROPERTY);
        return traceProperty == null || Boolean.parseBoolean(traceProperty);
    }
}