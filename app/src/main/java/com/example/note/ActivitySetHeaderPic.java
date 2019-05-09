package com.example.note;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.note.util.ImageUtil;
import com.example.note.util.UserUtil;

import java.io.File;
import java.io.IOException;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySetHeaderPic extends AppCompatActivity {

    private CircleImageView headerPic;
    private Uri imageUri;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_set_headerpic);
        headerPic = findViewById(R.id.set_header_image);
        textView = findViewById(R.id.header_set_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = textView.getText().toString();
                if(text.equals("确定")){
                    finish();
                }
            }
        });

        Glide.with(ActivitySetHeaderPic.this)
                .load(UserUtil.user.getUserHeaderPic())
                .error(R.drawable.touxiang)
                .dontAnimate()
                .into(headerPic);

        headerPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setHeaderPic();
            }
        });
    }

    public void setHeaderPic(){
        final String[] items = new String[] { "相册","照相机"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySetHeaderPic.this);
        // 设置参数
        builder.setIcon(R.drawable.xuanze).setTitle("选择头像")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            Intent intent = new Intent(Intent.ACTION_PICK, null);
                            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                            startActivityForResult(intent, 100);
                        }else if(which==1){
                            File outputImage = new File(getExternalCacheDir(), ImageUtil.getCreateUrl() + ".jpg");
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(ActivitySetHeaderPic.this, "com.example.asus.watermelonenglish.fileprovider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            imageUri = Uri.fromFile(outputImage);
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, 1);
                        }
                    }
                });
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                UserUtil.user.setUserHeaderPic(data.getDataString());
                UserUtil.user.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            textView.setText("确定");
                            Toast.makeText(ActivitySetHeaderPic.this, "更新成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivitySetHeaderPic.this, "更新失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Glide.with(ActivitySetHeaderPic.this)
                        .load(data.getDataString())
                        .error(R.drawable.touxiang)
                        .dontAnimate()
                        .into(headerPic);
            }
        }
        if (resultCode == RESULT_OK && requestCode == 1) {
            UserUtil.user.setUserHeaderPic(imageUri.getPath());
            UserUtil.user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        textView.setText("确定");
                        Toast.makeText(ActivitySetHeaderPic.this, "更新成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ActivitySetHeaderPic.this, "更新失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            textView.setText("确定");
            Glide.with(ActivitySetHeaderPic.this)
                    .load(imageUri.getPath())
                    .error(R.drawable.touxiang)
                    .dontAnimate()
                    .into(headerPic);
        }
    }


}
