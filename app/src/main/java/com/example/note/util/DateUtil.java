package com.example.note.util;

import java.util.Calendar;

public class DateUtil {

    public static String today(){
        Calendar calendar = Calendar.getInstance();
        int yearTag = calendar.get(Calendar.YEAR);//当前年
        int monthTag = calendar.get(Calendar.MONTH)+1;//当前月
        int dayTag = calendar.get(Calendar.DAY_OF_MONTH);//当前日
        return yearTag+"/"+monthTag+"/"+dayTag;
    }
    public static String getCurrentYear(){
        String date=today();
        String[] str = date.split("/");
        return str[0];
    }
    public static String getCurrentMonth(){
        String date=today();
        String[] str = date.split("/");
        return str[1];
    }
    public static String getCurrentDay(){
        String date=today();
        String[] str = date.split("/");
        return str[2];
    }
    public static String getYear(String date){
        String[] str = date.split("/");
        return str[0];
    }
    public static String getMonth(String date){
        String[] str = date.split("/");
        return str[1];
    }
    public static String getDay(String date){
        String[] str = date.split("/");
        return str[2];
    }
}
