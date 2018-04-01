package com.javarush.task.task32.task3202;

import java.io.*;

/* 
Читаем из потока
*/
public class Solution {
    public static void main(String[] args) throws IOException {
        StringWriter writer = getAllDataFromInputStream(new FileInputStream("testFile.log"));
       // StringWriter writer = getAllDataFromInputStream(null);
        System.out.println(writer.toString());
    }

    public static StringWriter getAllDataFromInputStream(InputStream is) throws IOException {
        if (is != null) {
            try (StringWriter stringWriter = new StringWriter();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                while (reader.ready()) {
                    stringWriter.write(reader.readLine());
                }
                return stringWriter;
            }
        }
        return new StringWriter();
    }
}