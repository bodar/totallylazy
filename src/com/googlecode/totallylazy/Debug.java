package com.googlecode.totallylazy;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Debug {
    public static boolean inDebug() {
        return debugging();
    }

    public static boolean debugging() {
        return getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }
}