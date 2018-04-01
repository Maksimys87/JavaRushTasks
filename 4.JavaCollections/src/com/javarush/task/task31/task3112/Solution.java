package com.javarush.task.task31.task3112;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.*;

/* 
Загрузчик файлов
*/
public class Solution {

    public static void main(String[] args) throws IOException {
        Path passwords = downloadFile("https://javarush.ru/testdata/secretPasswords.txt", Paths.get("D:/MyDownloads"));

        for (String line : Files.readAllLines(passwords)) {
            System.out.println(line);
        }
    }

    public static Path downloadFile(String urlString, Path downloadDirectory) throws IOException {
        URL url = new URL(urlString);
        Path tempPath = Files.createTempFile(Paths.get("d:\\"),"temp-",".tmp");
        try(InputStream inputStream = url.openStream()) {
            Files.copy(inputStream,tempPath,StandardCopyOption.REPLACE_EXISTING);
        }
      //  if (tempPath.toFile().length() != 0) {
          //  Files.move(tempPath,downloadDirectory.resolveSibling(url.getFile()),StandardCopyOption.REPLACE_EXISTING);
        String fileName = urlString.substring(urlString.lastIndexOf("/") + 1);
        Path dowloadedFile = Paths.get(downloadDirectory.toString() + "/" + fileName);
        Files.createDirectories(downloadDirectory);
            Files.move(tempPath,dowloadedFile);
      //  }
        return dowloadedFile;
    }
}
