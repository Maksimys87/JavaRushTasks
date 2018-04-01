package com.javarush.task.task22.task2207;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/* 
Обращенные слова
*/
public class Solution {
    public static List<Pair> result = new LinkedList<>();

    public static void main(String[] args) {
        String fileName = null;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            fileName = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.ready()) {
                list.addAll(Arrays.asList(reader.readLine().split(" ")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            StringBuilder stringBuilder = new StringBuilder(iterator.next());
            String rev = stringBuilder.reverse().toString();
            while (iterator.hasNext()) {
                String temp = iterator.next();
                if (rev.equals(temp)) {
                    StringBuilder sb = new StringBuilder(rev);
                    result.add(new Pair(sb.reverse().toString(), temp));
                    iterator.remove();
                }
            }
        }

       /* for (String revers:list) {
            StringBuilder stringBuilder = new StringBuilder(revers);
            String rev = stringBuilder.reverse().toString();
            for (int i = 0; i < list.size(); i++) {
                if (rev.equals(list.get(i))) {
                    StringBuilder sb = new StringBuilder(rev);
                    result.add(new Pair(sb.reverse().toString(),list.remove(i)));
                }
            }
        }*/
        for (Pair p : result) {
            System.out.println(p.toString());
        }
    }

    public static class Pair {
        String first;
        String second;

        public Pair() {
        }

        public Pair(String first, String second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pair pair = (Pair) o;

            if (first != null ? !first.equals(pair.first) : pair.first != null) return false;
            return second != null ? second.equals(pair.second) : pair.second == null;

        }

        @Override
        public int hashCode() {
            int result = first != null ? first.hashCode() : 0;
            result = 31 * result + (second != null ? second.hashCode() : 0);
            return result;
        }

        @Override
        public String toString() {
            return first == null && second == null ? "" :
                    first == null && second != null ? second :
                            second == null && first != null ? first :
                                    first.compareTo(second) < 0 ? first + " " + second : second + " " + first;

        }
    }

}
