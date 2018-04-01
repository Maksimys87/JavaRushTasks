package com.javarush.task.task25.task2506;

public class LoggingStateThread extends Thread {
    Thread thread;
    public LoggingStateThread(Thread thread) {
        this.thread = thread;
        //setDaemon(true);
    }

    @Override
    public void run() {
        Thread.State s = thread.getState();
        System.out.println(s);
        while (thread.getState() != State.TERMINATED) {
            Thread.State s1 = thread.getState();
            if (s != s1) {
                System.out.println(s1);
                s = s1;
            }
        }
       // System.out.println(thread.getState());
    }
}
