package com.googlecode.totallylazy.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

public class NamedExecutors {
    public static ThreadFactory namedThreadFactory(String name, ThreadFactory threadFactory) {
        return new NamedThreadFactory(name, threadFactory);
    }

    public static ThreadFactory namedThreadFactory(String name) {
        return new NamedThreadFactory(name, Executors.defaultThreadFactory());
    }

    public static ThreadFactory namedThreadFactory(Class<?> creatingClass) {
        return namedThreadFactory(creatingClass.getName());
    }

    public static ExecutorService newFixedThreadPool(int nThreads, String name) {
        return Executors.newFixedThreadPool(nThreads, namedThreadFactory(name));
    }

    public static ExecutorService newFixedThreadPool(int nThreads, Class<?> creatingClass) {
        return Executors.newFixedThreadPool(nThreads, namedThreadFactory(creatingClass));
    }

    public static ExecutorService newSingleThreadExecutor(String name) {
        return Executors.newSingleThreadExecutor(namedThreadFactory(name));
    }

    public static ExecutorService newSingleThreadExecutor(Class<?> creatingClass) {
        return Executors.newSingleThreadExecutor(namedThreadFactory(creatingClass));
    }

    public static ExecutorService newCachedThreadPool(String name) {
        return Executors.newCachedThreadPool(namedThreadFactory(name));
    }

    public static ExecutorService newCachedThreadPool(Class<?> creatingClass) {
        return Executors.newCachedThreadPool(namedThreadFactory(creatingClass));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(String name) {
        return Executors.newSingleThreadScheduledExecutor(namedThreadFactory(name));
    }

    public static ScheduledExecutorService newSingleThreadScheduledExecutor(Class<?> creatingClass) {
        return Executors.newSingleThreadScheduledExecutor(namedThreadFactory(creatingClass));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, String name) {
        return Executors.newScheduledThreadPool(corePoolSize, namedThreadFactory(name));
    }

    public static ScheduledExecutorService newScheduledThreadPool(int corePoolSize, Class<?> creatingClass) {
        return Executors.newScheduledThreadPool(corePoolSize, namedThreadFactory(creatingClass));
    }
}