package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_qq).setOnClickListener(this);
        findViewById(R.id.login_user).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.login_qq:{
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                break;
            }
            case R.id.login_user:{
                startActivity(new Intent(LoginActivity.this,UserLoginActivity.class));
                break;
            }
            default:
                break;
        }
    }
}
