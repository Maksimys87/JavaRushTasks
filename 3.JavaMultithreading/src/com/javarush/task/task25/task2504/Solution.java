package com.javarush.task.task25.task2504;

/* 
Switch для нитей
*/
public class Solution {
    public static void processThreads(Thread... threads) {
        for (Thread thread : threads) {
            String s = thread.getState().toString();
            switch (s) {
                case "NEW" :
                    thread.start();
                    break;
                case "RUNNABLE" :
                    thread.isInterrupted();
                    break;
                case "BLOCKED" :
                    thread.interrupt();
                    break;
                case "WAITING" :
                    thread.interrupt();
                    break;
                case "TIMED_WAITING" :
                    thread.interrupt();
                    break;
                case "TERMINATED" :
                    System.out.println(thread.getPriority());
                    break;
            }
        }
    }

    public static void main(String[] args) {
    }
}
