package com.javarush.task.task30.task3010;

/* 
Минимальное допустимое основание системы счисления
*/

import java.math.BigInteger;

public class Solution {
    public static void main(String[] args) {
        String result = null;
        for (int i = 2; i <= 36; i++) {
            try {
                result = new BigInteger(args[0],i).toString();
                System.out.println(i);
                break;
            } catch (Exception e) {
            }
        }
        if (result == null) System.out.println("incorrect");
    }
}