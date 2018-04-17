package com.javarush.task.task36.task3605;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

/* 
Использование TreeSet
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        TreeSet<Character> treeSet = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            while (reader.ready()) {
                char[] chars = reader.readLine().toLowerCase().toCharArray();
                for (char c : chars) {
                    if (c > 96) treeSet.add(c);
                }
            }
        }

        Iterator<Character> iterator = treeSet.iterator();
        for (int i = 0; iterator.hasNext(); i++) {
            if (i > 4) return;
            System.out.print(iterator.next());
        }
    }
}
