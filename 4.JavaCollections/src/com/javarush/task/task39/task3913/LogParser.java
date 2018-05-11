package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.DateQuery;
import com.javarush.task.task39.task3913.query.EventQuery;
import com.javarush.task.task39.task3913.query.IPQuery;
import com.javarush.task.task39.task3913.query.UserQuery;

import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery {
    private Path logDir;

    public LogParser(Path logDir) {
        this.logDir = logDir;
    }

    private static class LogParserData {
        private String ip;
        private String name;
        private Date date;
        private Event event;
        private int taskNumber;
        private Status status;

        LogParserData(String ip, String name, Date date, Event event, int taskNumber, Status status) {
            this.ip = ip;
            this.name = name;
            this.date = date;
            this.event = event;
            this.taskNumber = taskNumber;
            this.status = status;
        }
    }

    private List<String> getStringsFromLogFiles() {
        List<String> list = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDir, "*.log")) {
            for (Path entry : stream) {
                try (BufferedReader reader = new BufferedReader(new FileReader(entry.toFile()))) {
                    while (reader.ready()) {
                        list.add(reader.readLine());
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private LogParserData getDataFromLine(String line, Date after, Date before) {
        try {
            Pattern pattern = Pattern.compile("(?<ip>\\d+\\.\\d+\\.\\d+\\.\\d+)\\s+(?<name>[a-zA-Zа-яА-Я ]+)\\s+" +
                    "(?<date>\\d+\\.\\d+\\.\\d+ \\d+:\\d+:\\d+)\\s+(?<event>[A-Z_]+)(\\s(?<taskNumber>\\d+))?\\s+(?<status>[A-Z]+)");
            Matcher matcher = pattern.matcher(line);
            if (matcher.find()) {
                Date date = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").parse(matcher.group("date"));
                Event event = Event.valueOf(matcher.group("event"));
                Status status = Status.valueOf(matcher.group("status"));
                int taskNumber = 0;
                if (matcher.group("taskNumber") != null)
                    taskNumber = Integer.parseInt(matcher.group("taskNumber"));
                LogParserData logParserHelper = new LogParserData(matcher.group("ip"),
                        matcher.group("name"), date, event, taskNumber, status);
                long ms = date.getTime();
                if (after == null && before == null) {
                    return logParserHelper;
                } else {
                    if (after == null) {
                        if (ms <= before.getTime()) {
                            return logParserHelper;
                        }
                    } else {
                        if (before == null) {
                            if (ms >= after.getTime()) {
                                return logParserHelper;
                            }
                        } else {
                            if (ms >= after.getTime() && ms <= before.getTime()) {
                                return logParserHelper;
                            }
                        }
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getNumberOfUniqueIPs(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.ip);
        }
        return set.size();
    }

    @Override
    public Set<String> getUniqueIPs(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.ip);
        }
        return set;
    }

    @Override
    public Set<String> getIPsForUser(String user, Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user))
                set.add(data.ip);
        }
        return set;
    }

    @Override
    public Set<String> getIPsForEvent(Event event, Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && event == data.event)
                set.add(data.ip);
        }
        return set;
    }

    @Override
    public Set<String> getIPsForStatus(Status status, Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && status == data.status)
                set.add(data.ip);
        }
        return set;
    }

    @Override
    public Set<String> getAllUsers() {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, null, null);
            if (data != null)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public int getNumberOfUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.name);
        }
        return set.size();
    }

    @Override
    public int getNumberOfUserEvents(String user, Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user))
                set.add(data.event);
        }
        return set.size();
    }

    @Override
    public Set<String> getUsersForIP(String ip, Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.ip.equals(ip))
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getLoggedUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.LOGIN == data.event)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getDownloadedPluginUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.DOWNLOAD_PLUGIN == data.event)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getWroteMessageUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.WRITE_MESSAGE == data.event)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.SOLVE_TASK == data.event)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getSolvedTaskUsers(Date after, Date before, int task) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.SOLVE_TASK == data.event && task == data.taskNumber)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.DONE_TASK == data.event)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<String> getDoneTaskUsers(Date after, Date before, int task) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Event.DONE_TASK == data.event && task == data.taskNumber)
                set.add(data.name);
        }
        return set;
    }

    @Override
    public Set<Date> getDatesForUserAndEvent(String user, Event event, Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && event == data.event && data.name.equals(user))
                set.add(data.date);
        }
        return set;
    }

    @Override
    public Set<Date> getDatesWhenSomethingFailed(Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Status.FAILED == data.status)
                set.add(data.date);
        }
        return set;
    }

    @Override
    public Set<Date> getDatesWhenErrorHappened(Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Status.ERROR == data.status)
                set.add(data.date);
        }
        return set;
    }

    @Override
    public Date getDateWhenUserLoggedFirstTime(String user, Date after, Date before) {
        TreeSet<Date> treeSet = new TreeSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user) && Event.LOGIN == data.event &&
                    Status.OK == data.status)
                treeSet.add(data.date);
        }
        return treeSet.size() > 0 ? treeSet.first() : null;
    }

    @Override
    public Date getDateWhenUserSolvedTask(String user, int task, Date after, Date before) {
        TreeSet<Date> treeSet = new TreeSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user) && Event.SOLVE_TASK == data.event &&
                    task == data.taskNumber)
                treeSet.add(data.date);
        }
        return treeSet.size() > 0 ? treeSet.first() : null;
    }

    @Override
    public Date getDateWhenUserDoneTask(String user, int task, Date after, Date before) {
        TreeSet<Date> treeSet = new TreeSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user) && Event.DONE_TASK == data.event &&
                    task == data.taskNumber)
                treeSet.add(data.date);
        }
        return treeSet.size() > 0 ? treeSet.first() : null;
    }

    @Override
    public Set<Date> getDatesWhenUserWroteMessage(String user, Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user) && Event.WRITE_MESSAGE == data.event)
                set.add(data.date);
        }
        return set;
    }

    @Override
    public Set<Date> getDatesWhenUserDownloadedPlugin(String user, Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user) && Event.DOWNLOAD_PLUGIN == data.event)
                set.add(data.date);
        }
        return set;
    }

    @Override
    public int getNumberOfAllEvents(Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.event);
        }
        return set.size();
    }

    @Override
    public Set<Event> getAllEvents(Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.event);
        }
        return set;
    }

    @Override
    public Set<Event> getEventsForIP(String ip, Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.ip.equals(ip))
                set.add(data.event);
        }
        return set;
    }

    @Override
    public Set<Event> getEventsForUser(String user, Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.name.equals(user))
                set.add(data.event);
        }
        return set;
    }

    @Override
    public Set<Event> getFailedEvents(Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Status.FAILED == data.status)
                set.add(data.event);
        }
        return set;
    }

    @Override
    public Set<Event> getErrorEvents(Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && Status.ERROR == data.status)
                set.add(data.event);
        }
        return set;
    }

    @Override
    public int getNumberOfAttemptToSolveTask(int task, Date after, Date before) {
        int result = 0;
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && task == data.taskNumber && Event.SOLVE_TASK == data.event)
                result++;
        }
        return result;
    }

    @Override
    public int getNumberOfSuccessfulAttemptToSolveTask(int task, Date after, Date before) {
        int result = 0;
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && task == data.taskNumber && Event.DONE_TASK == data.event)
                result++;
        }
        return result;
    }

    @Override
    public Map<Integer, Integer> getAllSolvedTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.taskNumber != 0 && Event.SOLVE_TASK == data.event) {
                if (map.containsKey(data.taskNumber)) {
                    map.put(data.taskNumber, map.get(data.taskNumber) + 1);
                } else map.put(data.taskNumber, 1);
            }
        }
        return map;
    }

    @Override
    public Map<Integer, Integer> getAllDoneTasksAndTheirNumber(Date after, Date before) {
        Map<Integer, Integer> map = new HashMap<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.taskNumber != 0 && Event.DONE_TASK == data.event) {
                if (map.containsKey(data.taskNumber)) {
                    map.put(data.taskNumber, map.get(data.taskNumber) + 1);
                } else map.put(data.taskNumber, 1);
            }
        }
        return map;
    }
}