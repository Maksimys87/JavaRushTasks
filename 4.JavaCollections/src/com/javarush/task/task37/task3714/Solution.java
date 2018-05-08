package com.javarush.task.task37.task3714;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashMap;
import java.util.Map;

/* 
Древний Рим
*/
public class Solution {
    private static final Map<Character, Integer> MAP = new LinkedHashMap<>();

    static {
        MAP.put('I', 1);
        MAP.put('V', 5);
        MAP.put('X', 10);
        MAP.put('L', 50);
        MAP.put('C', 100);
        MAP.put('D', 500);
        MAP.put('M', 1000);
    }

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Input a roman number to be converted to decimal: ");
        String romanString = bufferedReader.readLine();
        System.out.println("Conversion result equals " + romanToInteger(romanString));
    }

    public static int romanToInteger(String s) {
        if (s.length() == 0) {
            System.out.println("Вы не ввели число");
            return -1;
        }
        int result = 0;
        char[] chars = s.toCharArray();
        for (char c : chars) {
            if (!MAP.containsKey(c)) {
                System.out.println("Вы ввели неверные символы");
                return -1;
            }
        }
        if (chars.length > 1) {
            if (((s.contains("VV")) || (s.contains("LL")) || (s.contains("DD"))) ||
                    ((chars.length > 3) && (s.contains("IIII") || (s.contains("XXXX") || (s.contains("CCCC") || (s.contains("MMMM"))))))) {
                System.out.println("Вы неверно ввели число");
                return -1;
            }
            for (int i = chars.length - 1; i >= 0; i--) {
                if (i == 0) {
                    result += MAP.get(chars[i]);
                } else {
                    int current = MAP.get(chars[i]);
                    int previous = MAP.get(chars[i - 1]);
                    if (current > previous) {
                        int diff = current - previous;
                        if ((previous == 1 || previous == 10 || previous == 100)
                                && (diff == 4 || diff == 9 || diff == 40 || diff == 90 || diff == 400 || diff == 900)
                                && !(((i - 2) >= 0) && (previous == MAP.get(chars[i - 2])))) {
                            result += current - previous;
                            --i;
                        } else {
                            System.out.println("Вы неверно ввели число");
                            return -1;
                        }
                    } else result += current;
                }
            }
        } else {
            result += MAP.get(chars[0]);
        }
        return result;
    }

}
