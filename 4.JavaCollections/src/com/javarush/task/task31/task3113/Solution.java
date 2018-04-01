package com.javarush.task.task31.task3113;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

/* 
Что внутри папки?
*/
public class Solution {
   /* private static  int dir = 0;
    private static int file = 0;
    private static long count = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Path path = Paths.get(reader.readLine());
        if (!Files.isDirectory(path)) {
            System.out.println(path + " - не папка");
        }
        else isDirectory(path);
        System.out.println("Всего папок - " + dir);
        System.out.println("Всего файлов - " + file);
        System.out.println("Общий размер - " + count);
    }
    public static void isDirectory(Path path) throws IOException {
        List<Path> list = Files.list(path).collect(Collectors.toList());
        for (Path p: list) {
            if (Files.isDirectory(p)) {
                dir++;
                isDirectory(p);
            }
            else {
                file++;
                count += p.toFile().length();
            }
        }
    }*/
   public static void main(String[] args) throws IOException {
       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
       Path path = Paths.get(reader.readLine());
       if (!Files.isDirectory(path)) {
           System.out.println(path + " - не папка");
       }
       else {
           final int[] foldersCounter = {0};
           final int[] filesCounter = {0};
           final int[] sizeCounter = {0};
           Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
               @Override
               public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                   foldersCounter[0]++;
                   return FileVisitResult.CONTINUE;
               }

               @Override
               public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                   filesCounter[0]++;
                   sizeCounter[0] += attrs.size();
                   return FileVisitResult.CONTINUE;
               }
           });
           System.out.println("Всего папок - " + (foldersCounter[0]-1));
           System.out.println("Всего файлов - " + filesCounter[0]);
           System.out.println("Общий размер - " + sizeCounter[0]);
       }
   }
}
