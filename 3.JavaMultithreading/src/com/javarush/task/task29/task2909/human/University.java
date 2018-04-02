package com.javarush.task.task29.task2909.human;

import java.util.ArrayList;
import java.util.List;

public class University {
    private List<Student> students = new ArrayList<>();
    private String name;
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public University(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student getStudentWithAverageGrade(double averageGrade) {
        //TODO:
        for (Student std : students) {
            if (std.getAverageGrade() == averageGrade) return std;
        }
        return null;
    }

    public Student getStudentWithMaxAverageGrade() {
        //TODO:
        double max = 0;
        for (Student std : students) {
            if (std.getAverageGrade() > max) max = std.getAverageGrade();
        }
        for (Student std : students) {
            if (std.getAverageGrade() == max) return std;
        }
        return null;
    }

    public Student getStudentWithMinAverageGrade() {
        double min = students.get(0).getAverageGrade();
        for (Student std : students) {
            if (std.getAverageGrade() < min) min = std.getAverageGrade();
        }
        for (Student std : students) {
            if (std.getAverageGrade() == min) return std;
        }
        return null;
    }

    public void expel(Student student) {
        students.remove(student);
    }
}