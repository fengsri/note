package com.example.note;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class NoteShowActivity extends AppCompatActivity {

    private TextView title;
    private TextView text;
    private TextView date;

    private String titlestr;
    private String textstr;
    private String datestr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_show);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标

        initData();
        initView();
    }

    private void initView() {
        title = findViewById(R.id.note_show_title);
        title.setText(titlestr);
        text = findViewById(R.id.note_show_text);
        text.setText(textstr);
        date = findViewById(R.id.note_show_date);
        date.setText(datestr);
        this.setTitle(titlestr);
    }


    private void initData() {
        titlestr = getIntent().getStringExtra("title");
        textstr = getIntent().getStringExtra("text");
        datestr = getIntent().getStringExtra("date");
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
