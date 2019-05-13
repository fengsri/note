package com.example.note.util;

import com.example.note.R;

public class StringUtil {
    public static String getRandString(){
        String[] strings= {
                "阿来 .艾伟 ","毕淑敏 ．柏杨","陈与义","陈启文","橙未央","蔡秋桐","戴叔伦","范锡林","方白羽 ","方白羽 ","方南江","甘世佳",
        "党益民","洪升 ","金克木","娇无那","黄桂元 "};
        int rand  =(int)(Math.random()*(strings.length));
        return strings[rand];
    }
}
