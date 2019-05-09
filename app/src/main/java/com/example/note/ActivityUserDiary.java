package com.example.note;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.note.dao.DiaryDao;
import com.example.note.dao.NoteDao;
import com.example.note.util.UserUtil;

public class ActivityUserDiary extends AppCompatActivity {

    private TextView self_diary_count;
    private TextView self_note_count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_diary);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标
        this.setTitle("基本信息");
        self_diary_count = findViewById(R.id.self_diary_count);
        self_note_count = findViewById(R.id.self_note_count);
        int diaryCount = DiaryDao.getDiaryFromLitePal(UserUtil.user.getObjectId()).size();
        int noteCount = NoteDao.getNoteFromLitePal(UserUtil.user.getObjectId()).size();
        self_diary_count.setText("记录日记"+diaryCount+"天");
        self_note_count.setText("便签总共"+noteCount+"个");
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
}
