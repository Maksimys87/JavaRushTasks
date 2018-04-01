package com.javarush.task.task28.task2813;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.*;

/* 
FutureTask
*/

public class Solution {
   // private static final ExecutorService threadpool = Executors.newFixedThreadPool(4);
   // private static final int n = 16;

    public static void main(String args[]) throws InterruptedException, ExecutionException {
       /* FactorialCalculator task = new FactorialCalculator(n);

        System.out.println("Submitting Task ...");
        Future future = threadpool.submit(task);
        System.out.println("Task was submitted successfully");

        while (!future.isDone()) {
            System.out.println("Task is not completed yet....");
            Thread.sleep(1);
        }

        System.out.println("Task is completed, let's check the result");
        long factorial = (long) future.get();

        System.out.println("Factorial of " + n + " is : " + factorial);
        threadpool.shutdown();*/

       HashSet<Integer> hashSet = createSet();
        hashSet.forEach(System.out::println);
        removeAllNumbersMoreThan10(hashSet);
        hashSet.forEach(System.out::println);
    }

    public static HashSet<Integer> createSet() {
       // ArrayList<Integer> list = new ArrayList<>();
        HashSet<Integer> set = new HashSet<>();

        for (int i = 0; i < 20; i++) {
            set.add(i);
        }

       // set.addAll(list);
        return set;
    }

    public static HashSet<Integer> removeAllNumbersMoreThan10(HashSet<Integer> set) {
        Iterator<Integer> iterator = set.iterator();
        while(iterator.hasNext()){
            int a = iterator.next();
            if(a > 10){
                iterator.remove();
            }
        }
        return set;
    }

}
