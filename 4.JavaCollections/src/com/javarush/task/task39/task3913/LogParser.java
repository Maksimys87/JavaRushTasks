package com.javarush.task.task39.task3913;

import com.javarush.task.task39.task3913.query.*;

import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogParser implements IPQuery, UserQuery, DateQuery, EventQuery, QLQuery {
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

    //================================================HelperMethods=====================================================//

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
                Date date = new SimpleDateFormat("d.M.y H:m:s").parse(matcher.group("date"));
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

    private Set<Date> getAllUniqueDates(Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.date);
        }
        return set;
    }

    private Set<Status> getAllUniqueStatuses(Date after, Date before) {
        Set<Status> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null)
                set.add(data.status);
        }
        return set;
    }

    private Set<String> getUsersForEventStatusDate(Event event, Status status, Date after, Date before) {
        Set<String> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null) {
                if (event == null && status == null) {
                    set.add(data.name);
                } else {
                    if (event == data.event && status == data.status)
                        set.add(data.name);
                    else {
                        if (event == data.event) set.add(data.name);
                        if (status == data.status) set.add(data.name);
                    }
                }
            }
        }
        return set;
    }

    private Set<Date> getDatesForIpOrUserOrEventOrStatus(String ip, String user, Event event, Status status, Date after, Date before) {
        Set<Date> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null) {
                if (data.ip.equals(ip)) set.add(data.date);
                if (data.name.equals(user)) set.add(data.date);
                if (data.event == event) set.add(data.date);
                if (data.status == status) set.add(data.date);
            }
        }
        return set;
    }

    private Set<Event> getEventsForStatus(Status status, Date after, Date before) {
        Set<Event> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null && data.status == status)
                set.add(data.event);
        }
        return set;
    }

    private Set<Status> getStatusesForIpOrUserOrEvent(String ip, String user, Event event, Date after, Date before) {
        Set<Status> set = new HashSet<>();
        for (String line : getStringsFromLogFiles()) {
            LogParserData data = getDataFromLine(line, after, before);
            if (data != null) {
                if (ip == null && user == null && event == null)
                    set.add(data.status);
                else {
                    if (data.ip.equals(ip)) set.add(data.status);
                    if (data.name.equals(user)) set.add(data.status);
                    if (data.event == event) set.add(data.status);
                }
            }
        }
        return set;
    }

    //================================================QLQuery=====================================================//

    @Override
    public Set<Object> execute(String query) {
        Set<Object> objects = new HashSet<>();
        String field1 = null, field2 = null, value = null;
        Date after = null, before = null, date = null;
        Status status = null;
        Event event = null;
        try {
            Pattern pattern = Pattern.compile("get (?<field1>\\w+) for (?<field2>\\w+) = \"(?<value>[\\w.: ]+)\" and date " +
                    "between \"(?<after>\\d+\\.\\d+\\.\\d+ \\d+:\\d+:\\d+)\" and \"(?<before>\\d+\\.\\d+\\.\\d+ \\d+:\\d+:\\d+)\"");
            Matcher matcher = pattern.matcher(query);
            if (matcher.find()) {
                field1 = matcher.group("field1");
                field2 = matcher.group("field2");
                value = matcher.group("value");
                after = new SimpleDateFormat("d.M.y H:m:s").parse(matcher.group("after"));
                before = new SimpleDateFormat("d.M.y H:m:s").parse(matcher.group("before"));
            } else {
                Pattern pat = Pattern.compile("get (?<field1>\\w+)" +
                        " for (?<field2>\\w+) = \"(?<value>[\\w.: ]+)\"");
                Matcher mat = pat.matcher(query);
                if (mat.find()) {
                    field1 = mat.group("field1");
                    field2 = mat.group("field2");
                    value = mat.group("value");
                } else {
                    Pattern p = Pattern.compile("get (?<field1>\\w+)");
                    Matcher m = p.matcher(query);
                    if (m.find())
                        field1 = m.group("field1");
                }
            }
            if (field1 != null) {
                if (field2 != null) {
                    if (field2.equals("date"))
                        date = new SimpleDateFormat("d.M.y H:m:s").parse(value);
                    if (field2.equals("event")) event = Event.valueOf(value);
                    if (field2.equals("status")) status = Status.valueOf(value);
                    if (field1.equals("ip")) {
                        if (field2.equals("user")) {
                            if (after != null && before != null) objects.addAll(getIPsForUser(value, after, before));
                            else objects.addAll(getIPsForUser(value, null, null));
                        }
                        if (field2.equals("date")) {
                            if (after != null && before != null) {
                                if (date.getTime() >= after.getTime() && date.getTime() <= before.getTime())
                                    objects.addAll(getUniqueIPs(date, date));
                            } else objects.addAll(getUniqueIPs(date, date));
                        }
                        if (field2.equals("event")) {
                            if (after != null && before != null) {
                                Date aft = new Date(after.getTime() + 1);
                                Date bef = new Date(before.getTime() - 1);
                                objects.addAll(getIPsForEvent(event, aft, bef));
                            } else objects.addAll(getIPsForEvent(event, null, null));
                        }
                        if (field2.equals("status")) {
                            if (after != null && before != null) {
                                Date aft = new Date(after.getTime() + 1);
                                Date bef = new Date(before.getTime() - 1);
                                objects.addAll(getIPsForStatus(status, aft, bef));
                            } else objects.addAll(getIPsForStatus(status, null, null));
                        }
                    }
                    if (field1.equals("user")) {
                        if (field2.equals("ip")) {
                            if (after != null && before != null) objects.addAll(getUsersForIP(value, after, before));
                            else objects.addAll(getUsersForIP(value, null, null));
                        }
                        if (field2.equals("date")) {
                            if (after != null && before != null) {
                                if (date.getTime() >= after.getTime() && date.getTime() <= before.getTime())
                                    objects.addAll(getUsersForEventStatusDate(null, null, date, date));
                            } else objects.addAll(getUsersForEventStatusDate(null, null, date, date));
                        }
                        if (field2.equals("event")) {
                            if (after != null && before != null)
                                objects.addAll(getUsersForEventStatusDate(event, null, after, before));
                            else objects.addAll(getUsersForEventStatusDate(event, null, null, null));
                        }
                        if (field2.equals("status")) {
                            if (after != null && before != null)
                                objects.addAll(getUsersForEventStatusDate(null, status, after, before));
                            else objects.addAll(getUsersForEventStatusDate(null, status, null, null));
                        }
                    }
                    if (field1.equals("date")) {
                        if (field2.equals("ip")) {
                            if (after != null && before != null)
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(value, null, null, null, after, before));
                            else
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(value, null, null, null, null, null));
                        }
                        if (field2.equals("user")) {
                            if (after != null && before != null)
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, value, null, null, after, before));
                            else
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, value, null, null, null, null));
                        }
                        if (field2.equals("event")) {
                            if (after != null && before != null) {
                                Date aft = new Date(after.getTime() + 1);
                                Date bef = new Date(before.getTime() - 1);
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, null, event, null, aft, bef));
                            } else
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, null, event, null, null, null));
                        }
                        if (field2.equals("status")) {
                            if (after != null && before != null)
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, null, null, status, after, before));
                            else
                                objects.addAll(getDatesForIpOrUserOrEventOrStatus(null, null, null, status, null, null));
                        }
                    }
                    if (field1.equals("event")) {
                        if (field2.equals("ip")) {
                            if (after != null && before != null) objects.addAll(getEventsForIP(value, after, before));
                            else objects.addAll(getEventsForIP(value, null, null));
                        }
                        if (field2.equals("user")) {
                            if (after != null && before != null) objects.addAll(getEventsForUser(value, after, before));
                            else objects.addAll(getEventsForUser(value, null, null));
                        }
                        if (field2.equals("date")) {
                            if (after != null && before != null) {
                                if (date.getTime() >= after.getTime() && date.getTime() <= before.getTime())
                                    objects.addAll(getAllEvents(date, date));
                            } else objects.addAll(getAllEvents(date, date));
                        }
                        if (field2.equals("status")) {
                            if (after != null && before != null)
                                objects.addAll(getEventsForStatus(status, after, before));
                            else objects.addAll(getEventsForStatus(status, null, null));
                        }
                    }
                    if (field1.equals("status")) {
                        if (field2.equals("ip")) {
                            if (after != null && before != null)
                                objects.addAll(getStatusesForIpOrUserOrEvent(value, null, null, after, before));
                            else objects.addAll(getStatusesForIpOrUserOrEvent(value, null, null, null, null));
                        }
                        if (field2.equals("user")) {
                            if (after != null && before != null)
                                objects.addAll(getStatusesForIpOrUserOrEvent(null, value, null, after, before));
                            else objects.addAll(getStatusesForIpOrUserOrEvent(null, value, null, null, null));
                        }
                        if (field2.equals("date")) {
                            if (after != null && before != null) {
                                if (date.getTime() >= after.getTime() && date.getTime() <= before.getTime())
                                    objects.addAll(getStatusesForIpOrUserOrEvent(null, null, null, date, date));
                            } else objects.addAll(getStatusesForIpOrUserOrEvent(null, null, null, date, date));
                        }
                        if (field2.equals("event")) {
                            if (after != null && before != null)
                                objects.addAll(getStatusesForIpOrUserOrEvent(null, null, event, after, before));
                            else objects.addAll(getStatusesForIpOrUserOrEvent(null, null, event, null, null));
                        }
                    }
                } else {
                    switch (field1) {
                        case "ip":
                            objects.addAll(getUniqueIPs(null, null));
                            break;
                        case "user":
                            objects.addAll(getAllUsers());
                            break;
                        case "date":
                            objects.addAll(getAllUniqueDates(null, null));
                            break;
                        case "event":
                            objects.addAll(getAllEvents(null, null));
                            break;
                        case "status":
                            objects.addAll(getAllUniqueStatuses(null, null));
                            break;
                    }
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return objects;
    }

    //================================================IPQuery=====================================================//

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

    //============================================UserQuery=====================================================//

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

    //============================================DateQuery=====================================================//

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

    //===========================================EventQuery=====================================================//

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