package com.javarush.task.task32.task3213;

import java.io.*;

/* 
Шифр Цезаря
*/

public class Solution {
    public static void main(String[] args) throws IOException {
        StringReader reader = new StringReader("Khoor#Dpljr#&C,₷B'3");
        System.out.println(decode(reader, -3));  //Hello Amigo #@)₴?$0u
    }

    public static String decode(StringReader reader, int key) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (reader != null) {
            try (BufferedReader br = new BufferedReader(reader)) {
                String str;
                while ((str = br.readLine()) != null) {
                    for (char chr : str.toCharArray()) {
                        builder.append((char) (chr + key));
                    }
                    // System.lineSeparator() на тот случай, если в StringReader будет содержаться несколько строк.
                    builder.append(System.lineSeparator());
                }
                return builder.toString().trim();
            }
        }
        return builder.toString();
    }
}
