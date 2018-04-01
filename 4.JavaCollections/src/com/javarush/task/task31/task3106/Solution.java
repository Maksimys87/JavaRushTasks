package com.javarush.task.task31.task3106;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipInputStream;

/*
Разархивируем файл
*/
public class Solution {
    public static void main(String[] args) throws IOException {
       /* String resultFileName = args[0];
        String[] strings = new String[args.length-1];
        for (int i = 1; i < args.length; i++) {
            strings[i-1] = args[i];
        }
        Arrays.sort(strings);
        List<FileInputStream> list = null;
        try {
            list = new ArrayList<>();
            for (int i = 0; i < args.length; i++) {
                list.add(new FileInputStream(strings[i]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1024];
        int count;
        try(ZipInputStream zipInputStream = new ZipInputStream(new SequenceInputStream(Collections.enumeration(list)));
            FileOutputStream fileOut = new FileOutputStream(new File(resultFileName))) {
            while (zipInputStream.getNextEntry() != null) {
                while ((count = zipInputStream.read(buffer)) > 0) {
                    fileOut.write(buffer, 0, count);
                    fileOut.flush();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/

        /*if (args.length < 2) return;

        String resultFileName = args[0];
        int filePartCount = args.length - 1;
        String[] fileNamePart = new String[filePartCount];
        for (int i = 0; i < filePartCount; i++)
        {
            fileNamePart[i] = args[i + 1];
        }
        Arrays.sort(fileNamePart);

        List<FileInputStream> fisList = new ArrayList<>();
        for (int i = 0; i < filePartCount; i++)
        {
            fisList.add(new FileInputStream(fileNamePart[i]));
        }
        SequenceInputStream seqInStream = new SequenceInputStream(Collections.enumeration(fisList));
        ZipInputStream zipInStream = new ZipInputStream(seqInStream);
        FileOutputStream fileOutStream = new FileOutputStream(resultFileName);
        byte[] buf = new byte[1024 * 1024];
        while (zipInStream.getNextEntry() != null)
        {
            int count;
            while ((count = zipInStream.read(buf)) != -1)
            {
                fileOutStream.write(buf, 0, count);
            }
        }
        seqInStream.close();
        zipInStream.close();
        fileOutStream.close();*/

        String[] myFiles = {"D:\\forJava\\MyArtifactName\\packForTest\\res2.txt",
                "D:\\forJava\\MyArtifactName\\packForTest\\res.txt",
                "D:\\forJava\\MyArtifactName\\packForTest\\res4.txt",
                "D:\\forJava\\MyArtifactName\\packForTest\\testDir\\"
        };
        String zipFile = "D:\\forJava\\MyArtifactName\\packForTest\\res.zip";
        ZipUtility zipUtil = new ZipUtility();
        try {
            zipUtil.zip(myFiles, zipFile);

        } catch (Exception ex) {
            // some errors occurred
            ex.printStackTrace();
        }
    }
}
