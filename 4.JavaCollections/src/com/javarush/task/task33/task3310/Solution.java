package com.javarush.task.task33.task3310;

import com.javarush.task.task33.task3310.strategy.*;
import com.javarush.task.task33.task3310.tests.FunctionalTest;
import com.javarush.task.task33.task3310.tests.SpeedTest;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Solution {
    public static Set<Long> getIds(Shortener shortener, Set<String> strings) {
        final Set<Long> set = new HashSet<>();
        strings.forEach(value -> set.add(shortener.getId(value)));
        return set;
    }

    public static Set<String> getStrings(Shortener shortener, Set<Long> keys) {
        final Set<String> set = new HashSet<>();
        keys.forEach(value -> set.add(shortener.getString(value)));
        return set;
    }

    public static void testStrategy(StorageStrategy strategy, long elementsNumber) {
        Helper.printMessage(strategy.getClass().getSimpleName());
        Set<String> randomStrings = new HashSet<>();
        for (int i = 0; i < elementsNumber; i++) {
            randomStrings.add(Helper.generateRandomString());
        }
        Shortener hashMapStorageStrategyShortener = new Shortener(strategy);
        long startGetIds = new Date().getTime();
        Set<Long> ids = getIds(hashMapStorageStrategyShortener, randomStrings);
        long finishGetIds = new Date().getTime();
        Helper.printMessage(String.valueOf(finishGetIds - startGetIds));
        long startGetStrings = new Date().getTime();
        Set<String> strings = getStrings(hashMapStorageStrategyShortener, ids);
        long finishGetStrings = new Date().getTime();
        Helper.printMessage(String.valueOf(finishGetStrings - startGetStrings));
        if (randomStrings.size() == strings.size())
            Helper.printMessage("Тест пройден.");
        else Helper.printMessage("Тест не пройден.");
    }

    public static void main(String[] args) {
        /*
        testStrategy(new HashMapStorageStrategy(), 10000L);
        testStrategy(new OurHashMapStorageStrategy(), 10000L);
        testStrategy(new FileStorageStrategy(), 100L);
        testStrategy(new OurHashBiMapStorageStrategy(), 10000L);
        testStrategy(new HashBiMapStorageStrategy(), 10000L);
        testStrategy(new DualHashBidiMapStorageStrategy(), 10000L);
        */
        FunctionalTest functionalTest = new FunctionalTest();
        functionalTest.testHashMapStorageStrategy();
        functionalTest.testOurHashMapStorageStrategy();
        functionalTest.testFileStorageStrategy();
        functionalTest.testOurHashBiMapStorageStrategy();
        functionalTest.testHashBiMapStorageStrategy();
        functionalTest.testDualHashBidiMapStorageStrategy();
        SpeedTest speedTest = new SpeedTest();
        speedTest.testHashMapStorage();
    }
}
