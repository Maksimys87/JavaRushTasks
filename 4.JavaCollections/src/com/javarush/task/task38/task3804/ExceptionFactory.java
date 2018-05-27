package com.javarush.task.task38.task3804;

public class ExceptionFactory {
    public static Throwable getException(Enum en) {
        if (en == null) return new IllegalArgumentException();
        String message = en.name().charAt(0) + en.name().substring(1).toLowerCase().replaceAll("_", " ");
        switch (en.getDeclaringClass().getSimpleName()) {
            case "ExceptionApplicationMessage":
                return new Exception(message);
            case "ExceptionDBMessage":
                return new RuntimeException(message);
            case "ExceptionUserMessage":
                return new Error(message);
            default:
                return new IllegalArgumentException();
        }
    }
}
