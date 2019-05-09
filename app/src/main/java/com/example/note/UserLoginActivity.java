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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.note.bean.User;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SQLQueryListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText name;
    private EditText password;
    private String name_str;
    private String password_str;
    private CircleImageView login_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_user);
        name = findViewById(R.id.login_user_name);
        password = findViewById(R.id.login_user_password);
        login_image = findViewById(R.id.login_image);

        findViewById(R.id.login_user_bt).setOnClickListener(this);
        findViewById(R.id.login_user_fg).setOnClickListener(this);
        findViewById(R.id.login_user_registe).setOnClickListener(this);

        name.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                } else {
                    // 此处为失去焦点时的处理内容
                    String regex = "^(\\w)+@(\\w)+(\\.)\\w+\\.?\\w+";
                    if ((name.getText().toString()).matches(regex)) {
                     //   Toast.makeText(UserLoginActivity.this,"邮箱",Toast.LENGTH_LONG).show();
                        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.addWhereEqualTo("email",name.getText().toString() );
                        categoryBmobQuery.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> object, BmobException e) {
                                if (e == null) {
                                    Glide.with(UserLoginActivity.this)
                                            .load(object.get(0).getUserHeaderPic())
                                            .error(R.drawable.luncher_bg2)
                                            .dontAnimate()
                                            .into(login_image);
                                } else {
                                }
                            }
                        });
                    } else {
                      //  Toast.makeText(UserLoginActivity.this,"用户名",Toast.LENGTH_LONG).show();
                        BmobQuery<User> categoryBmobQuery = new BmobQuery<>();
                        categoryBmobQuery.addWhereEqualTo("username",name.getText().toString() );
                        categoryBmobQuery.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> object, BmobException e) {
                                if (e == null) {
                                    Glide.with(UserLoginActivity.this)
                                            .load(object.get(0).getUserHeaderPic())
                                            .error(R.drawable.luncher_bg2)
                                            .dontAnimate()
                                            .into(login_image);
                                } else {
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_user_bt: {
                name_str = name.getText().toString();
                password_str = password.getText().toString();
                //loginByNamePwd();
                String regex = "^(\\w)+@(\\w)+(\\.)\\w+\\.?\\w+";
                if (name_str.matches(regex)) {
                    loginByEmailPwd();
                } else {
                    loginByNamePwd();
                }
                break;
            }
            case R.id.login_user_fg: {
                startActivity(new Intent(UserLoginActivity.this, ForgetActivity.class));
                break;
            }
            case R.id.login_user_registe: {
                startActivity(new Intent(UserLoginActivity.this, RegisterActivity.class));
                break;
            }
            default:
                break;
        }
    }



    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示！");
        builder.setMessage("你要退出软件吗？");
        builder.setIcon(R.mipmap.ic_launcher_round);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(true);
        //设置正面按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        //设置反面按钮
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 邮箱+密码登录
     */
    private void loginByEmailPwd() {
        //TODO 此处替换为你的邮箱和密码
        BmobUser.loginByAccount(name_str, password_str, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                    intent.putExtra("ato", 0);
                    startActivity(intent);
                    finish();
                } else {
                    name.setText("");
                    password.setText("");
                    Toast.makeText(UserLoginActivity.this, "邮箱或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 用户名+密码登录
     */
    private void loginByNamePwd() {
        //此处替换为你的用户名密码
        BmobUser.loginByAccount(name_str, password_str, new LogInListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    Intent intent = new Intent(UserLoginActivity.this, MainActivity.class);
                    intent.putExtra("ato", 0);
                    startActivity(intent);
                    finish();
                } else {
                    name.setText("");
                    password.setText("");
                    Toast.makeText(UserLoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
