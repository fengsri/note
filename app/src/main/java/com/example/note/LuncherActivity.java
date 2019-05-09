package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.example.note.bean.User;
import com.example.note.util.UserUtil;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class LuncherActivity extends AppCompatActivity {
    int finalTag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_luncher);
        Bmob.initialize(this, "8d772e6ff4de85b39b9e17869d5ef88f");
        //数据库初始化
        LitePal.getDatabase();

        int tag =0;
        User user = BmobUser.getCurrentUser(User.class);
        if(null == user){ //未登录
            tag=0;
        }else{  //登录
            tag=1;
            UserUtil.user = user;
        }
        final int finalTag = tag;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(finalTag ==0){
                    Intent intent=new Intent(LuncherActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    startActivity(new Intent(LuncherActivity.this,MainActivity.class));
                    finish();
                }
            }
        },2000);
    }

}
