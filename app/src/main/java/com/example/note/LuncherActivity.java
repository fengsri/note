package com.example.note;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.note.bean.User;
import com.example.note.util.UserUtil;

import org.litepal.LitePal;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

public class LuncherActivity extends AppCompatActivity {
    private TextView time1;
    private TextView tiao1;
    private int tag = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_luncher);
        Bmob.initialize(this, "8d772e6ff4de85b39b9e17869d5ef88f");
        //数据库初始化
        LitePal.getDatabase();

        tiao1 = findViewById(R.id.tiao_textview);
        time1 = findViewById(R.id.time_textview);

        User user = BmobUser.getCurrentUser(User.class);
        if (null == user) { //未登录
            tag = 0;
        } else {  //登录
            tag = 1;
            UserUtil.user = user;
        }

        final ValueAnimator animation = ValueAnimator.ofInt(5, 0);
        animation.setDuration(5 * 1000);//设置倒计时时长
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if (String.valueOf(animation.getAnimatedValue()) != null) {
                    int i = Integer.valueOf(String.valueOf(animation.getAnimatedValue()));
                    time1.setText(i+"");
                    if (i == 0) {
                        if (tag == 0) {
                            Intent intent = new Intent(LuncherActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                }
            }
        });
        animation.start();//倒计时开始

        tiao1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animation.cancel();
                if (tag == 0) {
                    Intent intent = new Intent(LuncherActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    startActivity(new Intent(LuncherActivity.this, MainActivity.class));
                    finish();
                }
            }
        });


//        final int finalTag = tag;
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if(finalTag ==0){
//                    Intent intent=new Intent(LuncherActivity.this,LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }else{
//                    startActivity(new Intent(LuncherActivity.this,MainActivity.class));
//                    finish();
//                }
//            }
//        },4000);
    }

}
