package com.example.note.mvp.model;

import com.example.note.bean.Diary;
import com.example.note.mvp.listener.MyListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class DiaryModel {

    /**
     * 保存数据到云
     *
     * @param userId
     * @param date
     * @param title
     * @param text
     * @param icon
     * @param pic
     * @param address
     * @param weather
     */
    public static void saveBeanDiaryToYun(final String userId, final String date, final String title, final String text, final String icon, final String pic, final String address, final String weather, final MyListener listener) {
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
            public void done(String objectId, BmobException e) {
                if (e == null) {
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
                    listener.sucess("成功");
                } else {
                    listener.error();
                }
            }
        });
    }



    /**
     * 书刷新本地数据
     *
     * @param userId
     */
    public static void refreshNewDiary(final String userId,final MyListener listener) {
        BmobQuery<Diary> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", userId);
        categoryBmobQuery.findObjects(new FindListener<Diary>() {
            @Override
            public void done(List<Diary> beanDiary, BmobException e) {
                if (e == null) {
                    List<com.example.note.domain.Diary> litePalDiary = com.example.note.dao.DiaryDao.getDiaryFromLitePal(userId);
                    if (litePalDiary == null && beanDiary != null) {
                        for (Diary diary : beanDiary) {
                            com.example.note.dao.DiaryDao.saveDiaryBeanToLitPal(diary);
                        }
                    }

                    if (litePalDiary != null && beanDiary != null) {
                        for (Diary diary : beanDiary) {
                            int i;
                            for (i = 0; i < litePalDiary.size(); i++) {
                                if (diary.getObjectId().equals(litePalDiary.get(i).getObjectId())) {
                                    break;
                                }
                            }
                            if (i == litePalDiary.size()) {
                                com.example.note.dao.DiaryDao.saveDiaryBeanToLitPal(diary);
                            }
                        }
                    }

                    listener.sucess("成功");
                }
                else {
                    listener.error();
                }
            }
        });
    }
}
