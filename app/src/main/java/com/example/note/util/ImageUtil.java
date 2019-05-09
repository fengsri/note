package com.example.note.util;

import com.example.note.R;

import java.util.Calendar;

public class ImageUtil {
    public static int getRandImageId(){
        int[] imageIds= {
                R.drawable.meiwenbg1,R.drawable.meiwenbg2,R.drawable.meiwenbg3,R.drawable.meiwenbg4,
                R.drawable.meiwenbg5,R.drawable.meiwenbg6,R.drawable.meiwenbg7,R.drawable.meiwenbg8,
                R.drawable.meiwenbg9,R.drawable.meiwenbg10,R.drawable.meiwenbg11,R.drawable.meiwenbg13};
        int rand  =(int)(Math.random()*(imageIds.length));
        return imageIds[rand];
    }

    public static String getCreateUrl(){
        Calendar calendar = Calendar.getInstance();
        int yearTag = calendar.get(Calendar.YEAR);//当前年
        int monthTag = calendar.get(Calendar.MONTH)+1;//当前月
        int dayTag = calendar.get(Calendar.DAY_OF_MONTH);//当前日
        int second = calendar.get(Calendar.SECOND);
        return yearTag+monthTag+dayTag+second+"";
    }
}
