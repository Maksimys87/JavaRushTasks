package com.javarush.task.task31.task3105;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/* 
Добавление файла в архив
*/
public class Solution {
    public static void main(String[] args) throws IOException {
      /*  Map<String,ByteArrayOutputStream> map = new HashMap<>();
        try(ZipInputStream inputStream = new ZipInputStream(new FileInputStream(args[1]));
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(args[1]))) {
            ZipEntry zipEntry;
            while ((zipEntry = inputStream.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                int count;
                byte[] buffer = new byte[1024];
                while ((count = inputStream.read(buffer)) > 0) {
                    byteArrayOutputStream.write(buffer,0,count);
                }
                map.put(zipEntry.getName(),byteArrayOutputStream);
            }
            Path file = Paths.get(args[0]);
            Path fileName = Paths.get(args[0].substring(args[0].lastIndexOf(File.separator)));
            for (Map.Entry<String,ByteArrayOutputStream> pair: map.entrySet()) {
                if (pair.getKey().substring(pair.getKey().lastIndexOf(File.separator)).equals(fileName)) continue;
                outputStream.putNextEntry(new ZipEntry(pair.getKey()));
                outputStream.write(pair.getValue().toByteArray());
            }
            outputStream.putNextEntry(new ZipEntry("new"+ fileName));
            Files.copy(file,outputStream);
        }*/
        String fileName = args[0];
        String zipFileName = args[1];
        File file = new File(fileName);

        Map<String, ByteArrayOutputStream> archivedFiles = new HashMap<>();
        try (ZipInputStream zipReader = new ZipInputStream(new FileInputStream(zipFileName))) {
            ZipEntry entry;
            while ((entry = zipReader.getNextEntry()) != null) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = zipReader.read(buffer)) != -1)
                    byteArrayOutputStream.write(buffer, 0, count);

                archivedFiles.put(entry.getName(), byteArrayOutputStream);
            }
        }

        try (ZipOutputStream zipWriter = new ZipOutputStream(new FileOutputStream(zipFileName))) {
            for (Map.Entry<String, ByteArrayOutputStream> pair : archivedFiles.entrySet()) {
                if (pair.getKey().substring(pair.getKey().lastIndexOf("/") + 1).equals(file.getName())) continue;
                zipWriter.putNextEntry(new ZipEntry(pair.getKey()));
                zipWriter.write(pair.getValue().toByteArray());
            }

            ZipEntry zipEntry = new ZipEntry("new/" + file.getName());
            zipWriter.putNextEntry(zipEntry);
            Files.copy(file.toPath(), zipWriter);
        }
    }
}
