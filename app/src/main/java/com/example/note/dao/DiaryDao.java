package com.example.note.dao;

import android.content.Context;
import android.widget.Toast;

import com.example.note.MainActivity;
import com.example.note.bean.Diary;

import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class DiaryDao {

    /**
     * 保存数据到本地Diary
     * @param diary
     */
    public static void saveDiaryBeanToLitPal(Diary diary){
        com.example.note.domain.Diary diaryS = new com.example.note.domain.Diary();
        diaryS.setObjectId(diary.getObjectId());
        diaryS.setDate(diary.getDate());
        diaryS.setAddress(diary.getAddress());
        diaryS.setIcon(diary.getIcon());
        diaryS.setPic(diary.getPic());
        diaryS.setText(diary.getText());
        diaryS.setWeather(diary.getWeather());
        diaryS.setTitle(diary.getTitle());
        diaryS.setUserId(diary.getUserId());
        diaryS.save();
    }


    /**
     * 保存数据到云
     * @param userId
     * @param date
     * @param title
     * @param text
     * @param icon
     * @param pic
     * @param address
     * @param weather
     * @param context
     */
    public static void saveBeanDiaryToYun(final String userId, final String date, final String title, final String text, final String icon, final String pic, final String address, final String weather, final Context context){
        final Diary diary = new Diary();
        diary.setUserId(userId);
        diary.setDate(date);
        diary.setAddress(address);
        diary.setIcon(icon);
        diary.setPic(pic);
        diary.setText(text);
        diary.setWeather(weather);
        diary.setTitle(title);
        diary.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    com.example.note.domain.Diary diaryS = new com.example.note.domain.Diary();
                    diaryS.setObjectId(objectId);
                    diaryS.setUserId(userId);
                    diaryS.setDate(date);
                    diaryS.setAddress(address);
                    diaryS.setIcon(icon);
                    diaryS.setPic(pic);
                    diaryS.setText(text);
                    diaryS.setWeather(weather);
                    diaryS.setTitle(title);
                    diaryS.save();
                    Toast.makeText(context,"添加数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"创建数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 保存数据到本地
     * @param userId
     * @param date
     * @param title
     * @param text
     * @param icon
     * @param pic
     * @param address
     * @param weather
     */
    public static void saveDiaryToLitPal(String objectId,String userId,String date,String title,String text,String icon,String pic,String address,String weather){
        com.example.note.domain.Diary diaryS = new com.example.note.domain.Diary();
        diaryS.setObjectId(objectId);
        diaryS.setDate(date);
        diaryS.setAddress(address);
        diaryS.setIcon(icon);
        diaryS.setPic(pic);
        diaryS.setText(text);
        diaryS.setWeather(weather);
        diaryS.setTitle(title);
        diaryS.setUserId(userId);
        diaryS.save();
    }

    /**
     * 通过userId获取本地的dairy
     * @param userId
     * @return
     */
    public static List<com.example.note.domain.Diary> getDiaryFromLitePal(String userId){
        List<com.example.note.domain.Diary> diaryList = DataSupport.where("userId = ?", userId).find(com.example.note.domain.Diary.class);
        return diaryList;
    }


    /**
     * 书刷新本地数据
     * @param userId
     */
    public static void refreshNewDiary(final String userId){
        BmobQuery<Diary> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", userId);
        categoryBmobQuery.findObjects(new FindListener<Diary>() {
            @Override
            public void done(List<Diary> beanDiary, BmobException e) {
                if (e == null) {
                    List<com.example.note.domain.Diary> litePalDiary = DiaryDao.getDiaryFromLitePal(userId);
                    if (litePalDiary == null && beanDiary != null) {
                        for (Diary diary : beanDiary) {
                            DiaryDao.saveDiaryBeanToLitPal(diary);
                        }
                    }

                    if (litePalDiary != null && beanDiary != null) {
                        for (Diary diary : beanDiary) {
                            int i;
                            for (i=0;i<litePalDiary.size();i++) {
                                if (diary.getObjectId().equals(litePalDiary.get(i).getObjectId())) {
                                    break;
                                }
                            }
                            if(i==litePalDiary.size()){
                                DiaryDao.saveDiaryBeanToLitPal(diary);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 删除本地数据
     * @param userId
     */
    public static void deleteDiaryFromLitePal(String userId){
        DataSupport.deleteAll(com.example.note.domain.Diary.class,"userId=?",userId);
    }

}
