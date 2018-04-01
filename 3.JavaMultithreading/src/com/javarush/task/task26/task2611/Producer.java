package com.javarush.task.task26.task2611;

import java.util.concurrent.ConcurrentHashMap;

public class Producer implements Runnable {
    private ConcurrentHashMap<String,String> map;

    public Producer(ConcurrentHashMap<String, String> map) {
        this.map = map;
    }

    @Override
    public void run() {
        try {
            for (int i = 1; i < Integer.MAX_VALUE; i++) {
                map.putIfAbsent(String.valueOf(i),("Some text for " + i));
                Thread.sleep(500);
            }
        }
        catch (InterruptedException e){
            System.out.println(String.format("[t%s] thread was terminated",Thread.currentThread().getName().split("t")[1]));
        }
    }
}
