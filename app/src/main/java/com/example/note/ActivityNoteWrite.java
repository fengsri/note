package com.example.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.example.note.dao.DiaryDao;
import com.example.note.dao.NoteDao;
import com.example.note.util.DateUtil;
import com.example.note.util.ImageUtil;
import com.example.note.util.UserUtil;

import java.io.File;
import java.io.IOException;

public class ActivityNoteWrite extends AppCompatActivity implements View.OnClickListener {

    private TextView noteTitle;
    private TextView noteText;
    private TextView noteDate;
    private TextView save;

    private String date;
    private String title;
    private String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        ActionBar supportActionBar = getSupportActionBar();
        this.setTitle("编辑便签");
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标
        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        noteDate = findViewById(R.id.note_write_date);
        noteDate.setText(DateUtil.today());
        save = findViewById(R.id.note_write_save);
        save.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.note_write_save:{
                save();
                finish();
                break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:////主键id 必须这样写
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void save(){
        date = noteDate.getText().toString();
        title = noteTitle.getText().toString();
        text = noteText.getText().toString();
        NoteDao.saveBeanNoteToYun(UserUtil.user.getObjectId(),date,title,text,null,null,null,null,ActivityNoteWrite.this);
    }
}
