package com.javarush.task.task23.task2305;

/* 
Inner
*/
public class Solution {
    public InnerClass[] innerClasses = new InnerClass[2];

    public class InnerClass {
    }

    public static Solution[] getTwoSolutions() {
        Solution solution = new Solution();
        Solution solution1 = new Solution();
        solution.innerClasses[0] = solution.new InnerClass();
        solution.innerClasses[1] = solution.new InnerClass();
        solution1.innerClasses[0] = solution.new InnerClass();
        solution1.innerClasses[1] = solution.new InnerClass();
        Solution[] solutions = {solution, solution1};
        return solutions;
    }

    public static void main(String[] args) {

    }
}
