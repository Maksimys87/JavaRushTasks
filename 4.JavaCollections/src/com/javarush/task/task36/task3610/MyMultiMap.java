package com.javarush.task.task36.task3610;

import java.io.Serializable;
import java.util.*;

public class MyMultiMap<K, V> extends HashMap<K, V> implements Cloneable, Serializable {
    static final long serialVersionUID = 123456789L;
    private HashMap<K, List<V>> map;
    private int repeatCount;

    public MyMultiMap(int repeatCount) {
        //if (repeatCount <= 0) throw new IllegalArgumentException("repeatCount должен быть больше нуля");
        this.repeatCount = repeatCount;
        map = new HashMap<>();
    }

    @Override
    public int size() {
        return values().size();
    }

    @Override
    public V put(K key, V value) {
        if (map.containsKey(key)) {
            List<V> values = map.get(key);
            if (values.size() < repeatCount) {
                values.add(value);
                return values.get(values.size() - 2);
            }
            values.remove(0);
            values.add(value);
            return values.get(values.size() - 2);

        }
        List<V> list = new ArrayList<>();
        list.add(value);
        map.put(key, list);
        return null;
    }

    @Override
    public V remove(Object key) {
        if (map.containsKey(key)) {
            List<V> values = map.get(key);
            V value = values.remove(0);
            if (values.size() == 0)
                map.remove(key);
            return value;
        }
        return null;
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        List<V> list = new ArrayList<>();
        for (List<V> value : map.values()) {
            list.addAll(value);
        }
        return list;
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return values().contains(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<K, List<V>> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            for (V v : entry.getValue()) {
                sb.append(v);
                sb.append(", ");
            }
        }
        String substring = sb.substring(0, sb.length() - 2);
        return substring + "}";
    }
}