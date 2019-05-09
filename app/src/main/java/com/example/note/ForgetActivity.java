package com.example.note;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener{
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget);
        email=findViewById(R.id.forget_email);
        Button forget_bt = findViewById(R.id.forget_bt);
        forget_bt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forget_bt:{
                String email_str= email.getText().toString();
                //loginByNamePwd();
                String regex = "^(\\w)+@(\\w)+(\\.)\\w+\\.?\\w+";
                if(email_str.matches(regex)) {
                    Toast.makeText(ForgetActivity.this, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                    resetPasswordByEmail(email_str);
                    Intent intent = new Intent(ForgetActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(ForgetActivity.this, "邮箱格式不正确",Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    /**
     * 邮箱重置密码
     */
    private void resetPasswordByEmail(final String email_str) {
        //TODO 此处替换为你的邮箱
        BmobUser.resetPasswordByEmail(email_str, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(ForgetActivity.this, "重置密码请求成功，请到邮箱进行密码重置操作",Toast.LENGTH_SHORT).show();
                } else {
                }
            }
        });
    }
}
