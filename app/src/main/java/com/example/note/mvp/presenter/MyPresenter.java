package com.example.note.mvp.presenter;

import android.content.Context;

import com.example.note.mvp.listener.MyListener;
import com.example.note.mvp.model.ArticleModel;
import com.example.note.mvp.model.DiaryModel;
import com.example.note.mvp.model.NoteModel;
import com.example.note.mvp.view.MyView;
import com.example.note.util.UserUtil;

public class MyPresenter implements MyListener {

    private MyView myView;
    private Context context;
    int tag = 0;
    public MyPresenter(MyView myView, Context context) {
        this.myView = myView;
        this.context = context;
    }

    public void refush(){
        myView.start();
        DiaryModel.refreshNewDiary(UserUtil.user.getObjectId(),this);
        NoteModel.refreshNewNote(UserUtil.user.getObjectId(),this);
        ArticleModel.refreshNewArticle(this);
    }

    @Override
    public void sucess(String data) {
        tag++;
        if(tag==3){
            myView.sucessful();
            myView.cancle();
        }
    }

    @Override
    public void error() {
        myView.error();
    }
}
