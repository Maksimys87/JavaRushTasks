package com.javarush.task.task26.task2601;

import java.util.Arrays;
import java.util.Comparator;

/*
Почитать в инете про медиану выборки
*/
public class Solution {

    public static void main(String[] args) {
        Integer[] ints = new Integer[]{13,9,15,5,17};
        for (Integer i:ints) {
            System.out.print(i + " ");
        }
        System.out.println();
        sort(ints);
        for (Integer i:ints) {
            System.out.print(i + " ");
        }
    }

    public static Integer[] sort(Integer[] array) {
        /*Arrays.sort(array);
        int med = 0;
        if (array.length%2 == 0) {
            med = (array[array.length/2] + array[array.length/2-1])/2;
        }
        else med = array[array.length/2];
        int mediana = med;
        Arrays.sort(array, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
               if(Integer.compare(Math.abs(o1-mediana),Math.abs(o2-mediana)) == 0)
                   return Integer.compare(o2,o1);
               else return Integer.compare(Math.abs(o1-mediana),Math.abs(o2-mediana));
            }
        });*/

        /*int median;
        Arrays.sort(array);
        if (array.length%2==0) median = (array[array.length/2] + array[array.length/2-1])/2;
        else median = array[array.length/2];
        Arrays.sort(array,(x,y)->
            Integer.compare(Math.abs(x-median),Math.abs(y-median)) == 0
                    ? Integer.compare(y,x)
                    : Integer.compare(Math.abs(x-median),Math.abs(y-median))
        );*/

        int median;
        Arrays.sort(array);
        //Если массив с четным колличеством элементов
        if (array.length%2==0) median = (array[array.length/2] + array[array.length/2-1])/2;
            //И если с нечетным
        else median = array[array.length/2];

        //Сортируем по условию
        Arrays.sort(array,(x,y)->Integer.compare(Math.abs(median-x), Math.abs(median-y)));
        return array;
    }
}
