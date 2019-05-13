package com.example.note;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.note.bean.User;
import com.example.note.util.DateUtil;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class RegisterQQActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText registe_name;
    private EditText registe_email;
    private Button registe_bt;

    private String name;
    private String password;
    private String email;
    private String headerPic;
    private String date;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registe_qq);
        init();
    }


    private void init() {
        registe_name=findViewById(R.id.registe_name);
        registe_email=findViewById(R.id.registe_email);
        registe_bt=findViewById(R.id.registe_bt);
        registe_bt.setOnClickListener(this);

        Intent intent=getIntent();
        headerPic=intent.getStringExtra("headerPic");
        name = intent.getStringExtra("name");
        registe_name.setText(name);
        password=name;
        date = DateUtil.today();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registe_bt:{
                name=registe_name.getText().toString();
                email=registe_email.getText().toString();

                if(email.equals("")){
                    Toast.makeText(RegisterQQActivity.this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
                    break;
                }else{
                    String regex = "^(\\w)+@(\\w)+(\\.)\\w+\\.?\\w+";
                    if(!email.matches(regex)){
                        Toast.makeText(RegisterQQActivity.this,"邮箱格式不正确！",Toast.LENGTH_SHORT).show();
                        break;
                    }
                }

                final MaterialDialog dialog = new MaterialDialog.Builder(this)
                        .title("注册中")
                        .content("Please Wait......")
                        .progress(true, 0)
                        .show();

                final User user = new User();
                user.setUsername(name);
                user.setPassword(password);
                user.setEmail(email);
                user.setUserHeaderPic(headerPic);
                user.setUserSignature("null");
                user.setUserUseDate(date);
                user.signUp(new SaveListener<User>() {
                    @Override
                    public void done(User user, BmobException e) {
                        if (e == null) {
                            login(name,password);
                            dialog.dismiss();
                        } else {
                            Toast.makeText(RegisterQQActivity.this,"登录失败，用户名或者邮箱已用",Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(RegisterQQActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

                break;
            }
            default:
                break;
        }
    }

    public void login(String name_str,String password_str){
        emailVerify();
        Toast.makeText(RegisterQQActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活账户",Toast.LENGTH_SHORT).show();
        BmobUser.loginByAccount(name_str, password_str, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent=new Intent(RegisterQQActivity.this,MainActivity.class);
                    intent.putExtra("ato",0);
                    startActivity(intent);
                    finish();
                    // Toast.makeText(RegisteActivity2.this,user.toString(),Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterQQActivity.this,"登录失败",Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterQQActivity.this, "请求验证邮件成功，请到" + email + "邮箱中进行激活账户",Toast.LENGTH_SHORT).show();
                } else {

                }
            }
        });
    }

}
