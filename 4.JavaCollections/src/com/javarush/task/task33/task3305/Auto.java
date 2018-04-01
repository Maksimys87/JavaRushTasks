package com.javarush.task.task33.task3305;


import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "className")
public abstract class Auto {
    protected String name;
    protected String owner;
    protected int age;
}