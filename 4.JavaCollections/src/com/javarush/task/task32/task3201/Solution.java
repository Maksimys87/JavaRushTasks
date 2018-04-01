package com.javarush.task.task32.task3201;

import java.io.IOException;
import java.io.RandomAccessFile;

/*
Запись в существующий файл
*/
public class Solution {
    public static void main(String... args) {
        try(RandomAccessFile raf = new RandomAccessFile(args[0],"rw")) {
          //  System.out.println(raf.length());
            if (raf.length() < Long.parseLong(args[1])) {
                raf.seek(raf.length());
              //  System.out.println(raf.getFilePointer());
                raf.write(args[2].getBytes());
             //   System.out.println(raf.length());
            } else {
                raf.seek(Long.parseLong(args[1]));
              //  System.out.println(raf.getFilePointer());
                raf.write(args[2].getBytes());
              //  System.out.println(raf.length());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
