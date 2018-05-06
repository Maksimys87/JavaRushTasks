package com.javarush.task.task33.task3310.tests;

import com.javarush.task.task33.task3310.Helper;
import com.javarush.task.task33.task3310.Shortener;
import com.javarush.task.task33.task3310.strategy.HashBiMapStorageStrategy;
import com.javarush.task.task33.task3310.strategy.HashMapStorageStrategy;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class SpeedTest {
    @Test
    public void testHashMapStorage() {
        Shortener shortener1 = new Shortener(new HashMapStorageStrategy());
        Shortener shortener2 = new Shortener(new HashBiMapStorageStrategy());
        Set<String> origStrings = new HashSet<>();
        Set<Long> ids = new HashSet<>();
        for (int i = 0; i < 10000; i++) {
            origStrings.add(Helper.generateRandomString());
        }
        long getTimeForGettingIdsForShortener1 = getTimeForGettingIds(shortener1, origStrings, ids);
        long getTimeForGettingIdsForShortener2 = getTimeForGettingIds(shortener2, origStrings, ids);
        Assert.assertNotEquals(getTimeForGettingIdsForShortener1, getTimeForGettingIdsForShortener2);
        long getTimeForGettingStringsForShortener1 = getTimeForGettingStrings(shortener1, ids, origStrings);
        long getTimeForGettingStringsForShortener2 = getTimeForGettingStrings(shortener2, ids, origStrings);
        Assert.assertEquals(getTimeForGettingStringsForShortener1, getTimeForGettingStringsForShortener2, 30);
    }

    public long getTimeForGettingIds(Shortener shortener, Set<String> strings, Set<Long> ids) {
        long startGetIds = new Date().getTime();
        for (String str : strings) {
            ids.add(shortener.getId(str));
        }
        long finishGetIds = new Date().getTime();
        return finishGetIds - startGetIds;
    }

    public long getTimeForGettingStrings(Shortener shortener, Set<Long> ids, Set<String> strings) {
        long startGetStrings = new Date().getTime();
        for (Long id : ids) {
            strings.add(shortener.getString(id));
        }
        long finishGetStrings = new Date().getTime();
        return finishGetStrings - startGetStrings;
    }
}
