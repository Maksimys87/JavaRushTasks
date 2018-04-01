package com.javarush.task.task34.task3403;

/* 
Разложение на множители с помощью рекурсии
*/
public class Solution {
    public void recursion(int n) {
        int k = 2;
        while (k <= n) {
            if ((n % k) == 0) {
                if (k != n) {
                    System.out.print(k + " ");
                    recursion(n / k);
                } else {
                    System.out.print(k);
                }
                return;
            }
            k++;
        }
    }
}
