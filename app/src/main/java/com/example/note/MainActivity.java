package com.example.note;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.note.fragment.ArticleFragment;
import com.example.note.fragment.DiaryFragment;
import com.example.note.fragment.NoteFragment;
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

import org.litepal.LitePal;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener{

    private CoordinatorLayout app_bar_main_view;

    private RelativeLayout layoutDiary;
    private RelativeLayout layoutNote;
    private RelativeLayout layoutArticle;
    private ImageView diary;
    private ImageView note;
    private ImageView article;
    private TextView textDiary;
    private TextView textNote;
    private TextView textArticle;
    private Toolbar toolbar;
    private TextView titleText;
    private FloatingActionButton fab;

    private IWXAPI api;
    private Tencent mTencent;
    private BaseUiListener mIUiListener=new BaseUiListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //数据库初始化
        LitePal.getDatabase();
        //第一：默认初始化微信
        api = WXAPIFactory.createWXAPI(this, "wxf371098a435d7f2b");
        api.registerApp("wxf371098a435d7f2b");
        //qq的默认
        mTencent = Tencent.createInstance("101572837", this.getApplicationContext());
    }

    public void initView(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleText = findViewById(R.id.title_text);
        app_bar_main_view = findViewById(R.id.app_bar_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                int drawerwidth=drawerView.getMeasuredWidth();
                int left_x=(int)(drawerwidth*slideOffset);
                app_bar_main_view.setLeft(left_x);
                app_bar_main_view.setScaleY((float)(1-0.3*slideOffset));
                app_bar_main_view.setScaleX((float)(1-0.3*slideOffset));
            }
        });

        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        diary = findViewById(R.id.home_diary);
        note = findViewById(R.id.home_note);
        article = findViewById(R.id.home_article);
        layoutDiary = findViewById(R.id.home_diary2);
        layoutDiary.setOnClickListener(this);
        layoutNote = findViewById(R.id.home_note2);
        layoutNote.setOnClickListener(this);
        layoutArticle = findViewById(R.id.home_article2);
        layoutArticle.setOnClickListener(this);
        textDiary = findViewById(R.id.home_diary_text);
        textNote = findViewById(R.id.home_note_text);
        textArticle = findViewById(R.id.home_article_text);

        setBackgroud(1);
        replace(new DiaryFragment());
        titleText.setText(R.string.bottom_text1);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_diary2:{
                setBackgroud(1);
                replace(new DiaryFragment());
                titleText.setText(R.string.bottom_text1);
                break;
            }
            case R.id.home_note2:{
                setBackgroud(2);
                replace(new NoteFragment());
                titleText.setText(R.string.bottom_text2);
                break;
            }
            case R.id.home_article2:{
                setBackgroud(3);
                replace(new ArticleFragment());
                titleText.setText(R.string.bottom_text3);
                break;
            }
            default:
                break;
        }
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

    //碎片切换
    public void replace(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction replace = manager.beginTransaction().replace(R.id.contentfragmelayout, fragment);
        replace.commit();
    }

    public void setBackgroud(int tag){
        diary.setImageResource(R.drawable.diary);
        note.setImageResource(R.drawable.note);
        article.setImageResource(R.drawable.article);
        textDiary.setTextColor(Color.parseColor("#2b2b2b"));
        textNote.setTextColor(Color.parseColor("#2b2b2b"));
        textArticle.setTextColor(Color.parseColor("#2b2b2b"));
        if(tag==1){
            diary.setImageResource(R.drawable.diary2);
            textDiary.setTextColor(Color.parseColor("#5299f5"));
        }else if(tag==2){
            note.setImageResource(R.drawable.note2);
            textNote.setTextColor(Color.parseColor("#5299f5"));
        }else if(tag==3){
            article.setImageResource(R.drawable.article2);
            textArticle.setTextColor(Color.parseColor("#5299f5"));
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
        intent.putExtra(Intent.EXTRA_TEXT,text);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setComponent(new ComponentName("com.tencent.mobileqq","com.tencent.mobileqq.activity.JumpActivity"));
        startActivity(intent);
    }

    public void shareToQZone(String text) {
        final Bundle params;
        params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, "打卡");// 标题
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, text);// 摘
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL,"http://www.qq.com/news/1.html");// 内容地址
        // 分享操作要在主线程中完成
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTencent.shareToQQ(MainActivity.this, params, new BaseUiListener());
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

    @SuppressLint("RestrictedApi")
    public void hinden(int tag){
        if(tag==1){
            fab.setVisibility(View.VISIBLE);
        }else if(tag==2){
            fab.setVisibility(View.INVISIBLE);
        }
    }

}
