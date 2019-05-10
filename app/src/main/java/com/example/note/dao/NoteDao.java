package com.example.note.dao;

import android.content.Context;
import android.widget.Toast;

import com.example.note.bean.Diary;
import com.example.note.bean.Note;
import org.litepal.crud.DataSupport;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class NoteDao {
    /**
     * 保存远程的获取的Note
     * @param note
     */
    public static void saveBeanNoteToLitPal(Note note){
        com.example.note.domain.Note noteS = new com.example.note.domain.Note();
        noteS.setObjectId(note.getObjectId());
        noteS.setDate(note.getDate());
        noteS.setAddress(note.getAddress());
        noteS.setIcon(note.getIcon());
        noteS.setPic(note.getPic());
        noteS.setText(note.getText());
        noteS.setWeather(note.getWeather());
        noteS.setTitle(note.getTitle());
        noteS.setUserId(note.getUserId());
        noteS.save();
    }


    /**
     * 保存本地Note
     *
     */
    public static void saveNoteToLitPal(String objectId,String userId,String date,String title,String text,String icon,String pic,String address,String weather){
        com.example.note.domain.Note noteS = new com.example.note.domain.Note();
        noteS.setObjectId(objectId);
        noteS.setDate(date);
        noteS.setAddress(address);
        noteS.setIcon(icon);
        noteS.setPic(pic);
        noteS.setText(text);
        noteS.setWeather(weather);
        noteS.setTitle(title);
        noteS.setUserId(userId);
        noteS.save();
    }

    /**
     * 通过userId获取本地Note
     * @param userId
     * @return
     */
    public static List<com.example.note.domain.Note> getNoteFromLitePal(String userId){
        List<com.example.note.domain.Note> noteList = DataSupport.where("userId = ?", userId).find(com.example.note.domain.Note.class);
        return noteList;
    }
    public static List<com.example.note.domain.Note> getNoteFromLitePal(String userId,List<com.example.note.domain.Note> noteList2){
        List<com.example.note.domain.Note> noteList = DataSupport.where("userId = ?", userId).find(com.example.note.domain.Note.class);
        noteList2.clear();
        for(com.example.note.domain.Note note:noteList){
            noteList2.add(note);
        }
        return noteList2;
    }
    /**
     * 保存Note到云
     * @param userId
     * @return
     */
    public static void saveBeanNoteToYun(final String userId, final String date, final String title, final String text, final String icon, final String pic, final String address, final String weather, final Context context){
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
            public void done(String objectId,BmobException e) {
                if(e==null){
                    NoteDao.saveNoteToLitPal(objectId,userId,date,title,text,icon,pic,address,weather);
                    Toast.makeText(context,"添加数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"创建数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 将远程的数据更新到本地
     * @param userId
     */
    public static void refreshNewNote(final String userId){
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
                }
            }
        });
    }


    /**
     * 删除本地Note
     * @param userId
     */
    public static void deleteNoteFromLitePal(String userId){
        DataSupport.deleteAll(com.example.note.domain.Note.class,"userId=?",userId);
    }


}
