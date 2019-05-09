package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DiaryShowActivity  extends AppCompatActivity implements View.OnClickListener{

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageView;
    private TextView titleText;
    private TextView weatherText;
    private TextView textText;
    private TextView addressText;
    private FloatingActionButton floatingActionButton;

    private String date;
    private String title;
    private String text;
    private String pic;
    private String address;
    private String weather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_show);
        initData();
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.diary_show_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(date);
        //头部
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        imageView = findViewById(R.id.diary_show_image);
        imageView.setImageResource(R.drawable.luncher_bg2);

        titleText = findViewById(R.id.dairy_show_title);
        titleText.setText(title);
        weatherText = findViewById(R.id.dairy_show_weather);
        weatherText.setText(weather);
        textText = findViewById(R.id.dairy_show_text);
        textText.setText(text);
        addressText = findViewById(R.id.dairy_show_address);
        addressText.setText(address);

        floatingActionButton = findViewById(R.id.dairy_show_float);
        floatingActionButton.setOnClickListener(this);
    }


    private void initData() {
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        title= intent.getStringExtra("title");
        text= intent.getStringExtra("text");
        pic= intent.getStringExtra("pic");
        address= intent.getStringExtra("address");
        weather= intent.getStringExtra("weather");
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dairy_show_float:{

                break;
            }
            default:
                break;
        }
    }
}
