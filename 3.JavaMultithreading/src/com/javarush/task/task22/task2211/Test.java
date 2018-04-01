package com.javarush.task.task22.task2211;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Test {
    public static void main(String[] args) throws IOException {
       /* byte[] buffer = new byte[1000];
        String example = "У ДжаваРаш ошибочка в задаче-;)";
        buffer = example.getBytes("Windows-1251");
        String result = new String(buffer,"UTF-8");
        String result1 = new String(buffer,"Windows-1251");
        System.out.println(result);
        System.out.println(result1);*/
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String astr = reader.readLine();
        String bstr = reader.readLine();
        String cstr = reader.readLine();
        double a = Integer.parseInt(astr)*1.0;
        double b = Integer.parseInt(bstr)*1.0;
        double c = Integer.parseInt(cstr)*1.0;
        String result=res(a,b,c);
        System.out.println(result);

    }
    public static String res(double a,double b,double c){
       /* if(a>b&&a>c){
            if(b+c>a){
                return "Треугольник существует.";
            }else{
                return "Треугольник не существует.";}
        }
        else if(c>b&&c>a){
            if(b+a>c){
                return "Треугольник существует.";
            }else{
                return "Треугольник не существует."; }
        }
        else if(b>a&&b>c){
            if(a+c>b){
                return "Треугольник существует.";
            }else{
                return "Треугольник не существует.";}
        }
        else{
            return "Треугольник не существует.";}*/
       if (((a+b)>c) && ((a+c)>b) && ((b+c)>a)) return "yes";
       return "no";

    }
}
