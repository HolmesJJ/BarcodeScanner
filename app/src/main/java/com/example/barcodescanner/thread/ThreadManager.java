package com.example.barcodescanner.thread;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ThreadManager {
    private ScheduledThreadPoolExecutor scheduleExec;

    private ThreadManager() {
        this.scheduleExec = new ScheduledThreadPoolExecutor(4, new BSThreadFactory(5, "BS_ThreadManager_"));
    }

    public static final ThreadManager getThreadPoolProxy() {
        return ThreadManager.SingletonHolder.INSTANCE;
    }

    public void execute(Runnable command) {
        this.scheduleExec.execute(command);
    }

    public void execute(List<Runnable> commands) {
        Iterator var2 = commands.iterator();

        while(var2.hasNext()) {
            Runnable command = (Runnable)var2.next();
            this.scheduleExec.submit(command);
        }

    }

    public void shutDown() {
        this.scheduleExec.shutdown();
    }

    public List<Runnable> shutDownNow() {
        return this.scheduleExec.shutdownNow();
    }

    public boolean isShutDown() {
        return this.scheduleExec.isShutdown();
    }

    public boolean isTerminated() {
        return this.scheduleExec.isTerminated();
    }

    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return this.scheduleExec.awaitTermination(timeout, unit);
    }

    public <T> Future<T> submit(Callable<T> task) {
        return this.scheduleExec.submit(task);
    }

    public <T> Future<T> submit(Runnable task, T result) {
        return this.scheduleExec.submit(task, result);
    }

    public Future<?> submit(Runnable task) {
        return this.scheduleExec.submit(task);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException {
        return this.scheduleExec.invokeAll(tasks);
    }

    public <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException {
        return this.scheduleExec.invokeAll(tasks, timeout, unit);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        return this.scheduleExec.invokeAny(tasks);
    }

    public <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return this.scheduleExec.invokeAny(tasks, timeout, unit);
    }

    public ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit) {
        return this.scheduleExec.schedule(command, delay, unit);
    }

    public <V> ScheduledFuture<V> schedule(Callable<V> callable, long delay, TimeUnit unit) {
        return this.scheduleExec.schedule(callable, delay, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        return this.scheduleExec.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        return this.scheduleExec.scheduleWithFixedDelay(command, initialDelay, delay, unit);
    }

    private static class SingletonHolder {
        private static final ThreadManager INSTANCE = new ThreadManager();

        private SingletonHolder() {
        }
    }
}
