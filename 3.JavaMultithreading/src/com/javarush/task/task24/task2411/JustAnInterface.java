package com.javarush.task.task24.task2411;

public interface JustAnInterface {
    public static final B B = new B();

    class B extends C {
        static {
            System.out.print("st");
        }

        public B() {
            System.out.print("B");
        }
    }
}