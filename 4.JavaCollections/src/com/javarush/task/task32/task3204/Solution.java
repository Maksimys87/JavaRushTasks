package com.javarush.task.task32.task3204;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/* 
Генератор паролей
*/
public class Solution {
    public static void main(String[] args) {
        ByteArrayOutputStream password = getPassword();
        System.out.println(password.toString());
    }

    public static ByteArrayOutputStream getPassword() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>();
        byte[] bytes = new byte[8];
        do {
            list.clear();
            for (int i = 0; i < 8; i++)
                list.add(random.nextInt(3));
        }
        while (!(list.contains(0) && list.contains(1) && list.contains(2)));
        for (int i = 0; i < 8; i++) {
            switch (list.get(i)) {
                case 0:
                    bytes[i] = (byte) random.ints(8, 48, 58).toArray()[i];
                    break;
                case 1:
                    bytes[i] = (byte) random.ints(8, 65, 91).toArray()[i];
                    break;
                case 2:
                    bytes[i] = (byte) random.ints(8, 97, 123).toArray()[i];
                    break;
            }
        }
        try {
            byteArrayOutputStream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream;
    }
}