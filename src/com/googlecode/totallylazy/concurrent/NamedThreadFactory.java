package com.googlecode.totallylazy.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.lang.String.format;

public class NamedThreadFactory implements ThreadFactory {
    private final String name;
    private final ThreadFactory threadFactory;

    public NamedThreadFactory(String name, ThreadFactory threadFactory) {
        this.threadFactory = threadFactory;
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread thread = threadFactory.newThread(runnable);
        thread.setName(format("%s: %s", name, thread.getName()));
        return thread;
    }
}