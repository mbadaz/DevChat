package com.app.devchat;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadHelper<T> {

    public ThreadHelper() {

    }

    public T runBackgroundTask(Callable<T> task, int numberOfTasks){
        T value = null;
        ExecutorService es = Executors.newFixedThreadPool(numberOfTasks);
        Future<T> futureT = es.submit(task);
        try {
            value = futureT.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return value;
    }

}
