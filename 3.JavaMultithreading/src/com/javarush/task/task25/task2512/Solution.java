package com.javarush.task.task25.task2512;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Живем своим умом
*/
public class Solution extends Thread implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        t.interrupt();
        List<Throwable> list = new ArrayList<>();
        Throwable throwable = e;
        while (throwable != null) {
            list.add(throwable);
            throwable = throwable.getCause();
        }
        Collections.reverse(list);
        for (Throwable tr : list) {
            System.out.println(tr);
        }
    }


    public void run() {
        this.uncaughtException(Thread.currentThread(), new Exception("ABC", new RuntimeException("DEF", new IllegalAccessException("GHI"))));
    }

    public static void main(String[] args) {
        Thread.UncaughtExceptionHandler handler = new Solution();
        Solution solution = new Solution();
        solution.setUncaughtExceptionHandler(handler);
        solution.start();
    }
}

