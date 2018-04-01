package com.javarush.task.task31.task3101;

import java.io.*;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/*
Проход по дереву файлов
*/
public class Solution {
    private static List<File> list = new ArrayList<>();
    public static void main(String[] args) {
       // Path path = Paths.get(args[0]);
        File pathFile = new File(args[0]);
        File resultFileAbsolutePath = new File(args[1]);
        File allFilesContent = new File(resultFileAbsolutePath.getParent() +"/allFilesContent.txt");
       // File pathFile = path.toFile();
        FileUtils.renameFile(resultFileAbsolutePath,allFilesContent);
       // filesIterator(pathFile);
       // Collections.sort(list,(x,y) -> x.getName().compareTo(y.getName()));
       // Collections.sort(list, Comparator.comparing(File::getName));
      /*  try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(resultFileAbsolutePath))){
            for (File file:list) {
                try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    while (reader.ready()) {
                        bufferedWriter.write(reader.readLine());
                        bufferedWriter.write("\\n");
                    }
                }
            }
        }*/
        try(FileOutputStream fileOutputStream = new FileOutputStream(allFilesContent)) {
            filesIterator(pathFile);
            Collections.sort(list, Comparator.comparing(File::getName));
            for (File file:list) {
                try(FileInputStream fileInputStream = new FileInputStream(file)) {
                    byte[] buf = new byte[1024];
                    while (fileInputStream.available() > 0) {
                        int len = fileInputStream.read(buf);
                        fileOutputStream.write(buf,0,len);
                    }
                    fileOutputStream.write(System.lineSeparator().getBytes());
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void filesIterator(File file) {
        for (File f:file.listFiles()) {
            if (f.isDirectory()) {
                filesIterator(f);
                continue;
            }
            else if (f.length() <= 50) {
                list.add(f);
            }
            else FileUtils.deleteFile(f);
        }
    }
}
