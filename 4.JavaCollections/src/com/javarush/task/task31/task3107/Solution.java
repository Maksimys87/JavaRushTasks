package com.javarush.task.task31.task3107;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/*
Null Object Pattern
*/
public class Solution {
    private FileData fileData;

    public Solution(String pathToFile) {
        try {
            boolean hidden = Files.isHidden(Paths.get(pathToFile));
            boolean executable = Files.isExecutable(Paths.get(pathToFile));
            boolean directory = Files.isDirectory(Paths.get(pathToFile));
            boolean writable = Files.isWritable(Paths.get(pathToFile));
            fileData = new ConcreteFileData(hidden,executable,directory,writable);
        } catch (Exception e) {
            fileData = new NullFileData(e);
        }
    }

    public FileData getFileData() {
        return fileData;
    }
}
