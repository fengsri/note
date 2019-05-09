package com.example.note;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class GoodActivity extends AppCompatActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_good);
        ActionBar supportActionBar = getSupportActionBar();
        this.setTitle("推文");
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标

        int tag = getIntent().getIntExtra("tag",1);
        tag = (tag+1)%5;
        String url = "file:///android_asset/html/item_1.html";
        if(tag==0){
            url = "file:///android_asset/html/item_2.html";
        }else if(tag==1){
            url = "file:///android_asset/html/item_3.html";
        }else if(tag==2){
            url = "file:///android_asset/html/item_4.html";
        }else if(tag==3){
            url = "file:///android_asset/html/item_5.html";
        }else if(tag==4){
            url = "file:///android_asset/html/item_1.html";
        }

       webView = findViewById(R.id.mywebview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
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
