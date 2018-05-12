package com.javarush.task.task39.task3913;


import java.nio.file.Paths;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;


public class Solution {
    public static void main(String[] args) {
        LogParser logParser = new LogParser(Paths.get("c:/logs/"));
        /*System.out.println(logParser.getNumberOfUniqueIPs(null, null));


        // Set<String> set = logParser.getIPsForUser("Amigo", null, null);
        //  Set<String> set = logParser.getIPsForEvent(Event.DONE_TASK, null, null);
        Set<String> set = logParser.getLoggedUsers(null, null);
        for (String s : set) {
            System.out.println(s);
        }
        //  System.out.println(Event.DONE_TASK);
        //  System.out.println("DONE_TASK LOGIN".contains(Event.DONE_TASK.toString()));*/
        //TreeSet<Date> treeSet = new TreeSet<>();
        /*treeSet.add(new Date(12345));
        treeSet.add(new Date(14358974));
        treeSet.add(new Date(1456));
        treeSet.add(new Date(1543654656));
        treeSet.add(null);
        treeSet.add(null);
        treeSet.add(null);
        System.out.println(treeSet.first());
        System.out.println(treeSet.last());*/
       //Set<Object> objects = logParser.execute("get status for ip = \"127.0.0.1\"");
        Set<Object> objects = logParser.execute("get date");
        for (Object o : objects) {
            System.out.println(o);
        }
    }
}