package com.javarush.task.task32.task3209;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class HTMLFileFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
        String fileName = f.getName();
        if (f.isDirectory() || (fileName.substring((fileName.length()-5)).equalsIgnoreCase(".html")) ||
                (fileName.substring((fileName.length()-4)).equalsIgnoreCase(".htm")))
            return true;
        return false;
    }

    @Override
    public String getDescription() {
        return "HTML и HTM файлы";
    }
}
