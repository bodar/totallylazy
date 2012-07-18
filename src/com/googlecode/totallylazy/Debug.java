package com.googlecode.totallylazy;

import static java.lang.management.ManagementFactory.getRuntimeMXBean;

public class Debug {
    public static boolean inDebug() {
        return getRuntimeMXBean().getInputArguments().toString().contains("-agentlib:jdwp");
    }
}