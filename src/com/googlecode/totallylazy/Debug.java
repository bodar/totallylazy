package com.googlecode.totallylazy;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Debug {
    public static boolean inDebug() {
        return debugging();
    }

    public static boolean debugging() {
        return getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }

    public static void trace(Exception e) {if (debugging()) e.printStackTrace();}

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
}