package com.javarush.task.task30.task3012;

/* 
Получи заданное число
*/

public class Solution {
    public static void main(String[] args) {
        Solution solution = new Solution();
        //solution.createExpression(1234);
        System.out.println(Integer.toString(16,8));
    }

    public void createExpression(int number) {
        String result = number + " =";
        int degree3 = 1;
        while (number > 0) {
            int quotient  = number / 3;
            int remainder = number % 3;
            if (remainder == 0) {
                degree3 *= 3;
                number = quotient;
            } else {
                if (remainder == 1) {
                    result += (" + " + degree3);
                    degree3 *= 3;
                    number = quotient;
                }
                if (remainder == 2) {
                    result += (" - " + degree3);
                    degree3 *= 3;
                    number = quotient +1;
                }
            }
        }
        System.out.println(result);
    }
}