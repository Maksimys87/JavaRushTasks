package com.javarush.task.task32.task3203;

import java.io.PrintWriter;
import java.io.StringWriter;

/*
Пишем стек-трейс
*/
public class Solution {
    public static void main(String[] args) {
        String text = getStackTrace(new IndexOutOfBoundsException("fff"));
       // String text = getStackTrace(null);
        System.out.println(text);
    }

    public static String getStackTrace(Throwable throwable) {
        StringWriter writer = new StringWriter();
        if (throwable != null) {
            throwable.printStackTrace(new PrintWriter(writer));
        }
        return writer.toString();
    }
}