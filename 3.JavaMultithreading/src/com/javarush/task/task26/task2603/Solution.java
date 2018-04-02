package com.javarush.task.task26.task2603;

import java.util.Comparator;

/*
Убежденному убеждать других не трудно
*/
public class Solution {

    public static void main(String[] args) {

    }

    public static class CustomizedComparator implements Comparator<Object> {
        private Comparator<Object>[] comparators;

        public CustomizedComparator(Comparator<Object>... vararg) {
            this.comparators = vararg;
        }

        public int compare(Object o1, Object o2) {
            int res = 0;
            for (Comparator c : comparators) {
                res = c.compare(o1, o2);
                if (res != 0) break;
            }
            return res;
        }
    }
}
