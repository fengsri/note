package com.example.note;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class DiaryShowActivity  extends AppCompatActivity implements View.OnClickListener{

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView imageView;
    private TextView titleText;
    private TextView weatherText;
    private TextView textText;
    private TextView addressText;
    private FloatingActionButton floatingActionButton;

    private String date;
    private String title;
    private String text;
    private String pic;
    private String address;
    private String weather;
    private int errorImageId;

    private IWXAPI api;
    private Tencent mTencent;
    private BaseUiListener mIUiListener = new BaseUiListener();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_show);
        initData();
        initView();
        //第一：默认初始化微信
        api = WXAPIFactory.createWXAPI(this, "wx31255991882d79b5");
        api.registerApp("wx31255991882d79b5");
        //qq的默认
        mTencent = Tencent.createInstance("101572837", this.getApplicationContext());
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.diary_show_title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        this.setTitle(date);
        //头部
        collapsingToolbar = findViewById(R.id.collapsing_toolbar);
        imageView = findViewById(R.id.diary_show_image);
        Glide.with(DiaryShowActivity.this)
                .load(pic)
                .error(errorImageId)
                .dontAnimate()
                .into(imageView);
        titleText = findViewById(R.id.dairy_show_title);
        titleText.setText(title);
        weatherText = findViewById(R.id.dairy_show_weather);
        weatherText.setText(weather);
        textText = findViewById(R.id.dairy_show_text);
        textText.setText(text);
        addressText = findViewById(R.id.dairy_show_address);
        addressText.setText(address);

        floatingActionButton = findViewById(R.id.dairy_show_float);
        floatingActionButton.setOnClickListener(this);
    }


    private void initData() {
        Intent intent = getIntent();
        date = intent.getStringExtra("date");
        title= intent.getStringExtra("title");
        text= intent.getStringExtra("text");
        pic= intent.getStringExtra("pic");
        address= intent.getStringExtra("address");
        weather= intent.getStringExtra("weather");
        errorImageId =  intent.getIntExtra("errorImageId",R.drawable.meiwenbg3);
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE || resultCode == Constants.REQUEST_QZONE_SHARE || resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, mIUiListener);
            }
        }
    }

    public void share(final String data){
        final String[] items = new String[] { "微信朋友圈","微信群聊","QQ群聊","QQ空间"};
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(DiaryShowActivity.this);
        // 设置参数
        builder.setIcon(R.drawable.fenxiang).setTitle("分享")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            shareText("美文："+ data,0);
                        }else if(which==1){
                            shareText("美文："+ data,1);
                        }else if(which==2){
                            shareToQQ("美文："+ data);
                        }else if(which==3){
                            shareToQZone("美文："+data);
                        }
                    }
                });
        builder.create().show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dairy_show_float:{
                ObjectAnimator rotation=ObjectAnimator.ofFloat(floatingActionButton,"rotation",360,0);
                rotation.setDuration(1500);
                rotation.setInterpolator(new AnticipateOvershootInterpolator());
                rotation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        share(title);
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                rotation.start();
                break;
            }
            default:
                break;
        }
    }


    //分享文字
    public void shareText(String text, int tag) {
        //初始化一个 WXTextObject 对象，填写分享的文本内容
        WXTextObject textObj = new WXTextObject();
        textObj.text = text;

        //用 WXTextObject 对象初始化一个 WXMediaMessage 对象
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = text;

        SendMessageToWX.Req req = new SendMessageToWX.Req();

        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        if (tag == 0) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }

        //调用api接口，发送数据到微信
        api.sendReq(req);
    }

    //分享图片
    public void shareImage(int res, int tag) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), res);
        WXWebpageObject webpageObject = new WXWebpageObject();
        WXMediaMessage msg = new WXMediaMessage(webpageObject);
        msg.mediaObject = new WXImageObject(bmp);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        if (tag == 0) {
            req.scene = SendMessageToWX.Req.WXSceneTimeline;
        } else {
            req.scene = SendMessageToWX.Req.WXSceneSession;
        }
        api.sendReq(req);
    }

    public void shareToQQ(String text) {
//        final Bundle params;
//        params = new Bundle();
//        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
//        params.putString(QQShare.SHARE_TO_QQ_TITLE, "打卡");// 标题
//        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, text);// 摘要
//        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"http://www.qq.com/news/1.html");// 内容地址
//        // 分享操作要在主线程中完成
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
//            }
//        });

        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "纯文字分享");
        intent.putExtra(Intent.EXTRA_TEXT, text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.tencent.mobileqq", "com.tencent.mobileqq.activity.JumpActivity"));
        startActivity(intent);
    }

    public void shareToQZone(String text) {
        final Bundle params;
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "打卡");// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);// 摘
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, "http://www.qq.com/news/1.html");// 内容地址
        // 分享操作要在主线程中完成
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(DiaryShowActivity.this, params, new DiaryShowActivity.BaseUiListener());
            }
        });
    }

    /**
     * 调用SDK已经封装好的接口时，例如：登录、快速支付登录、应用分享、应用邀请等接口，需传入该回调的实例。
     */
    public class BaseUiListener implements IUiListener {
        @Override
        public void onCancel() {
            // TODO Auto-generated method stub
            //Toast.makeText(LoginActivity.this, "取消登陆", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onComplete(Object response) {
            // TODO Auto-generated method stub
            //   Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(UiError arg0) {
            // TODO Auto-generated method stub
            //Toast.makeText(LoginActivity.this, "登录失败"+arg0, Toast.LENGTH_LONG).show();
        }
    }
}
