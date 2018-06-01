package com.dekinci.eden.model;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Worker {
    private static final Worker w = new Worker();

    public static Worker getWorker() {
        return w;
    }

    private ExecutorService daemonWorker = Executors.newCachedThreadPool(r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
    });

    public Future<?> submit(Runnable task) {
        return daemonWorker.submit(task);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return daemonWorker.submit(task);
    }
}
