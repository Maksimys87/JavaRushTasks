package com.javarush.task.task31.task3111;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

/* 
Продвинутый поиск файлов
*/
public class Solution {

    public static void main(String[] args) throws IOException {
        SearchFileVisitor searchFileVisitor = new SearchFileVisitor();

        searchFileVisitor.setPartOfName("amigo");
        searchFileVisitor.setPartOfContent("programmer");
        searchFileVisitor.setMinSize(500);
        searchFileVisitor.setMaxSize(10000);

        Files.walkFileTree(Paths.get("D:/SecretFolder"), searchFileVisitor);

        List<Path> foundFiles = searchFileVisitor.getFoundFiles();
        for (Path file : foundFiles) {
            System.out.println(file);
        }
        //System.out.println(new String(new byte[]{71,111,111,100,32,66,121,101,33},"UTF-8"));
      /*  Path path = Paths.get("d:\\Test\\Tset\\Temp\\qwer5.txt");
        Path path1 = Paths.get("d:\\Test");
        Path path22 = Paths.get("d:\\Test\\Tset\\Temp\\qwer5.txt");
        Path path33 = Paths.get("d:\\Test\\Tset");
        Path path44 = Paths.get("qwer5.txt");
        Path path2 = path.relativize(path1);
        Path path3 = path1.relativize(path);
        Path path4 = path22.relativize(path1);
        Path path5 = path1.resolve(path33);
        Path path6 = path1.resolve(path44);
        Path path7 = path6.resolve(path33);
        System.out.println(path2);
        System.out.println(path4);
        System.out.println(path3);
        System.out.println(path5);
        System.out.println(path6);
        System.out.println(path7);
        URL url = new URL("https://javarush.ru/quests/lectures/questcollections.level01.lecture03");
        InputStream inputStream = url.openStream();

        Path tempFile = Files.createTempFile(path1,"YO","rrrrrrrrrr");
      //  Files.copy(inputStream, tempFile);
       Files.copy(inputStream,tempFile, StandardCopyOption.REPLACE_EXISTING);*/
    }

}
