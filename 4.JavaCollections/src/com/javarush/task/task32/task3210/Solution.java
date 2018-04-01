package com.javarush.task.task32.task3210;

import java.io.IOException;
import java.io.RandomAccessFile;

/* 
Используем RandomAccessFile
*/

public class Solution {
    public static void main(String... args) {
        try(RandomAccessFile raf = new RandomAccessFile(args[0],"rw")) {
            int lengthText = args[2].length();
            byte[] buffer = new byte[lengthText];
            raf.seek(Long.parseLong(args[1]));
            raf.read(buffer,0,lengthText);
            raf.seek(raf.length());
            if (new String(buffer).equals(args[2]))
                raf.write("true".getBytes());
            else raf.write("false".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
