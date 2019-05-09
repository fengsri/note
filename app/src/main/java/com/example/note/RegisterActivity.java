package com.example.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.note.bean.User;
import com.example.note.util.DateUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText registe_name;
    private EditText registe_email;
    private EditText registe_pd;
    private EditText registe_pd2;
    private Button registe_bt;

    private String name;
    private String email;
    private String password;
    private String password2;
    private String date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registe);
        registe_name=findViewById(R.id.registe_name);
        registe_email=findViewById(R.id.registe_email);
        registe_bt=findViewById(R.id.registe_bt);
        registe_pd=findViewById(R.id.registe_pd);
        registe_pd2=findViewById(R.id.registe_pd2);
        findViewById(R.id.registe_bt).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registe_bt:{

                name=registe_name.getText().toString();
                if(name.equals("")) {
                    Toast.makeText(RegisterActivity.this,"用户名不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String re="[A-Za-z0-9]{4,10}";
                    if(!name.matches(re)){
                        Toast.makeText(RegisterActivity.this,"用户名由4-10个字母数字构成！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                email=registe_email.getText().toString();
                if(email.equals("")){
                    Toast.makeText(RegisterActivity.this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String regex = "^(\\w)+@(\\w)+(\\.)\\w+\\.?\\w+";
                    if(!email.matches(regex)){
                        Toast.makeText(RegisterActivity.this,"邮箱格式不正确！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                password=registe_pd.getText().toString();
                password2=registe_pd2.getText().toString();
                if(password.equals("")){
                    Toast.makeText(RegisterActivity.this,"密码不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String re="[A-Za-z0-9]{4,10}";
                    if(!password.matches(re)){
                        Toast.makeText(RegisterActivity.this,"密码由4-10个字母数字构成！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(password2.equals("")){
                    Toast.makeText(RegisterActivity.this,"确认密码不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String re="[A-Za-z0-9]{4,10}";
                    if(!password2.matches(re)){
                        Toast.makeText(RegisterActivity.this,"确认密码由4-10个字母数字构成！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if(!password2.equals(password)){
                    Toast.makeText(RegisterActivity.this,"密码不一致！",Toast.LENGTH_SHORT).show();
                    break;
                }
                date = DateUtil.today();
                register();
                break;
            }
            default:
                break;
        }
    }

    public void register(){
        final User user = new User();
        user.setUsername(name);
        user.setPassword(password);
        user.setUserHeaderPic("null");
        user.setEmail(email);
        user.setUserSignature("null");
        user.setUserUseDate(date);
        emailVerify();
        Toast.makeText(RegisterActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活账户",Toast.LENGTH_SHORT).show();
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent=new Intent(RegisterActivity.this,UserLoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this,"注册失败:用户名或邮箱已存在",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 发送验证邮件
     */
    private void emailVerify() {
        //TODO 此处替换为你的邮箱
        BmobUser.requestEmailVerify(email, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(RegisterActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活账户",Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }
}
