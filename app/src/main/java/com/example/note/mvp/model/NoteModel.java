package com.example.note.mvp.model;

import com.example.note.bean.Note;
import com.example.note.dao.NoteDao;
import com.example.note.mvp.listener.MyListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class NoteModel {

    /**
     * 保存Note到云
     * @param userId
     * @return
     */
    public static void saveBeanNoteToYun(final String userId, final String date, final String title, final String text, final String icon, final String pic, final String address, final String weather,  final MyListener listener){
        Note note = new Note();
        note.setUserId(userId);
        note.setDate(date);
        note.setAddress(address);
        note.setIcon(icon);
        note.setPic(pic);
        note.setText(text);
        note.setWeather(weather);
        note.setTitle(title);
        note.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if(e==null){
                    NoteDao.saveNoteToLitPal(objectId,userId,date,title,text,icon,pic,address,weather);
                    listener.sucess("成功");
                }else{
                    listener.error();
                }
            }
        });
    }


    /**
     * 将远程的数据更新到本地
     * @param userId
     */
    public static void refreshNewNote(final String userId,final MyListener listener){
        BmobQuery<Note> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("userId", userId);
        categoryBmobQuery.findObjects(new FindListener<Note>() {
            @Override
            public void done(List<Note> beanNote, BmobException e) {
                if (e == null) {
                    List<com.example.note.domain.Note> litePalNote = NoteDao.getNoteFromLitePal(userId);
                    if (litePalNote == null && beanNote != null) {
                        for (Note note : beanNote) {
                            NoteDao.saveBeanNoteToLitPal(note);
                        }
                    }

                    if (litePalNote != null && beanNote != null) {
                        for (Note note : beanNote) {
                            int i;
                            for (i=0;i<litePalNote.size();i++) {
                                if (note.getObjectId().equals(litePalNote.get(i).getObjectId())) {
                                    break;
                                }
                            }
                            if(i==litePalNote.size()){
                                NoteDao.saveBeanNoteToLitPal(note);
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
