package com.example.note;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.note.adapter.ImagerViewPager;
import com.example.note.view.CircleIndicator2;

import java.util.ArrayList;
import java.util.List;

public class ArticleShowActivity extends AppCompatActivity {

    private TextView title;
    private TextView auther;
    private TextView text;
    private ViewPager viewPager;
    private CircleIndicator2 indicator2;


    private String titlestr;
    private String autherstr;
    private String textstr;

    private List<ImageView> list = new ArrayList<ImageView>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_show);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标

        initData();
        initView();
    }


    private void initView() {
        title = findViewById(R.id.article_show_title);
        title.setText(titlestr);
        auther = findViewById(R.id.article_show_auther);
        auther.setText(autherstr);
        text = findViewById(R.id.article_show_text);
        text.setText(textstr);

        indicator2 = findViewById(R.id.article_header_indicator_show);
        viewPager = findViewById(R.id.article_header_show);
        ImagerViewPager imagerViewPager=new ImagerViewPager(list);
        viewPager.setAdapter(imagerViewPager);
        indicator2.setUpWithViewPager(viewPager);
        this.setTitle(titlestr);
    }


    private void initData() {
          titlestr = getIntent().getStringExtra("title");
          autherstr= getIntent().getStringExtra("auther");
          textstr= getIntent().getStringExtra("text");
         int[] imageIds= {R.drawable.luncher_bg1,R.drawable.luncher_bg2,R.drawable.luncher_bg3,R.drawable.luncher_bg4,R.drawable.luncher_bg5,R.drawable.luncher_bg1};
         for(int i=0;i<3;i++){
             ImageView imageView=new ImageView(ArticleShowActivity.this);
             imageView.setScaleType(ImageView.ScaleType.FIT_XY);
             imageView.setImageResource(imageIds[(int)(Math.random()*(imageIds.length))]);
             imageView.setTag(i);
             list.add(imageView);
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

}



