package com.javarush.task.task35.task3505;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertableUtil {

    public static <K, V> Map<K, V> convert(List<? extends Convertable> list) {
        Map<K, V> result = new HashMap<>();
        for (Convertable c : list) {
            result.put((K) c.getKey(), (V) c);
        }
        return result;
    }
}
