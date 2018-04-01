package com.javarush.task.task22.task2211;


import java.io.*;

/* 
Смена кодировки
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        try (FileInputStream inputStream = new FileInputStream(args[0]);
             FileOutputStream outputStream = new FileOutputStream(args[1])) {
            while (inputStream.available() > 0) {
                byte[] buffer = new byte[1000];
                inputStream.read(buffer);
                String s = new String(buffer, "Windows-1251");
                System.out.println(s);
                buffer = s.getBytes("UTF-8");
                outputStream.write(buffer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
