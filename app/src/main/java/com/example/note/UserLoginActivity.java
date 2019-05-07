package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_user);
        findViewById(R.id.login_user_bt).setOnClickListener(this);
        findViewById(R.id.login_user_fg).setOnClickListener(this);
        findViewById(R.id.login_user_registe).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_user_bt:{
                startActivity(new Intent(UserLoginActivity.this,MainActivity.class));
                break;
            }
            case R.id.login_user_fg:{
                startActivity(new Intent(UserLoginActivity.this,ForgetActivity.class));
                break;
            }
            case R.id.login_user_registe:{
                startActivity(new Intent(UserLoginActivity.this,RegisterActivity.class));
                break;
            }
            default:
                break;
        }
    }
}
