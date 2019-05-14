package com.example.note;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.example.note.bean.Article;
import com.example.note.bean.Comment;
import com.example.note.bean.Diary;
import com.example.note.bean.Note;
import com.example.note.bean.User;
import com.example.note.dao.ArticleDao;
import com.example.note.dao.DiaryDao;
import com.example.note.dao.NoteDao;
import com.example.note.fragment.ArticleFragment;
import com.example.note.fragment.DiaryFragment;
import com.example.note.fragment.NoteFragment;
import com.example.note.mvp.presenter.MyPresenter;
import com.example.note.mvp.view.MyView;
import com.example.note.util.UserUtil;
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
import org.litepal.crud.DataSupport;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static cn.bmob.newim.util.IMLogger.init;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener , MyView {

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
    private CircleImageView headerPic;

    private IWXAPI api;
    private Tencent mTencent;
    private BaseUiListener mIUiListener = new BaseUiListener();
    private com.example.note.bean.User user;

    private MaterialDialog dialog ;
    /**
     * activity的创建
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //数据库初始化
        LitePal.getDatabase();

        user = BmobUser.getCurrentUser(com.example.note.bean.User.class);
        UserUtil.user = user;

        dialog = new MaterialDialog.Builder(this)
                .title("加载数据")
                .content("Please Wait......")
                .progress(true, 0).build();

        //刷新本地数据库的数据
        initData();
        //设置view
        initView();

        //第一：默认初始化微信
        api = WXAPIFactory.createWXAPI(this, "wx31255991882d79b5");
        api.registerApp("wx31255991882d79b5");
        //qq的默认
        mTencent = Tencent.createInstance("101572837", this.getApplicationContext());
    }


    /**
     * 初始化加载数据
     */
    private void initData() {
        /*DiaryDao.deleteDiaryFromLitePal(UserUtil.user.getObjectId());
        NoteDao.deleteNoteFromLitePal(UserUtil.user.getObjectId());
        ArticleDao.deleteDiaryFromLitePal();
         for(int i=1;i<6;i++){
            DiaryDao.saveBeanDiaryToYun(user.getObjectId(),
                    "2019/05/"+i,"这是日记标题"+i,
                    "这是日记内容这是日记内容这是日记内容这是日记内容这是日记内容这是日记内容这是日记内容这是日记内容这是日记内容",
                    "icon",
                    "pic",
                    "成都市西华大学",
                    "晴天",MainActivity.this);
        }

        for(int i=1;i<6;i++){
            NoteDao.saveBeanNoteToYun(user.getObjectId(),
                    "2019/05/09","这是便签标题"+i,
                    "这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容",
                    "icon",
                    "pic",
                    "成都市西华大学",
                    "晴天",
                    MainActivity.this);
        }

        for(int i=1;i<20;i++){
            ArticleDao.saveBeanArticleToYun("2019/05/"+i,"这娱乐"+i,
                    "这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容这是娱乐的内容",
                    "icon",
                    "pic1",
                    "pic2",
                    "pic3",
                    "1",MainActivity.this);
        }
        for(int i=1;i<20;i++){
            ArticleDao.saveBeanArticleToYun("2019/05/"+i,"这军事"+i,
                    "这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容这是军事内容",
                    "icon",
                    "pic1",
                    "pic2",
                    "pic3",
                    "2",MainActivity.this);
        }

        for(int i=1;i<20;i++){
            ArticleDao.saveBeanArticleToYun("2019/05/"+i,"这体育"+i,
                    "这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容这是体育的内容",
                    "icon",
                    "pic1",
                    "pic2",
                    "pic3",
                    "3",MainActivity.this);
        }

        for(int i=1;i<20;i++){
            ArticleDao.saveBeanArticleToYun("2019/05/"+i,"这情感"+i,
                    "这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容这是情感的内容",
                    "icon",
                    "pic1",
                    "pic2",
                    "pic3",
                    "4",MainActivity.this);
        }

        for(int i=1;i<20;i++){
            ArticleDao.saveBeanArticleToYun("2019/05"+i,"这社会"+i,
                    "这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容这是社会的内容",
                    "icon",
                    "pic1",
                    "pic2",
                    "pic3",
                    "5",MainActivity.this);
        }

        DiaryDao.refreshNewDiary(user.getObjectId());
        NoteDao.refreshNewNote(user.getObjectId());
        ArticleDao.refreshNewArticle();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                replace(new DiaryFragment());
                dialog.dismiss();
            }
        },2000);*/



/*        User user = BmobUser.getCurrentUser(User.class);
        Article article = new Article();
        article.setObjectId("ea8c3dd3be");
        final Comment comment = new Comment();
        comment.setComment("test");
        comment.setArticle(article);
        comment.setUser(user);
        comment.save(new SaveListener<String>() {

            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                   Toast.makeText(MainActivity.this,"评论发表成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MainActivity.this,"评论发表失败",Toast.LENGTH_SHORT).show();
                }
            }

        });*/

       /* BmobQuery<Comment> query = new BmobQuery<Comment>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        Article article = new Article();
        article.setObjectId("ea8c3dd3be");
        query.addWhereEqualTo("article",new BmobPointer(article));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("user");
        query.findObjects(new FindListener<Comment>() {

            @Override
            public void done(List<Comment> objects,BmobException e) {
                int a= 0;
            }
        });*/

        MyPresenter myPresenter = new MyPresenter(this,this);
        myPresenter.refush();
   }


    /**
     * 绑定view
     */
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        titleText = findViewById(R.id.title_text);
        app_bar_main_view = findViewById(R.id.app_bar_main);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                int drawerwidth = drawerView.getMeasuredWidth();
                int left_x = (int) (drawerwidth * slideOffset);
                app_bar_main_view.setLeft(left_x);
                app_bar_main_view.setScaleY((float) (1 - 0.3 * slideOffset));
                app_bar_main_view.setScaleX((float) (1 - 0.3 * slideOffset));
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

        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);
        headerPic = headerLayout.findViewById(R.id.home_header_pic);

        Glide.with(MainActivity.this)
                .load(user.getUserHeaderPic())
                .error(R.drawable.touxiang)
                .dontAnimate()
                .into(headerPic);
        TextView username = headerLayout.findViewById(R.id.home_username);
        TextView email = headerLayout.findViewById(R.id.home_email);
        username.setText(user.getUsername());
        email.setText(user.getEmail());

        setBackgroud(1);
        titleText.setText(R.string.bottom_text1);
    }


    /**
     * 点击返回调用
     */
    @Override
    public void onBackPressed() {
        new MaterialDialog.Builder(this)
                .title("提示！")
                .content("是否退出软件")
                .positiveText("确认")
                .negativeText("取消")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        // TODO
                    }
                })
                .show();
    }

    /**
     * 创建菜单
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private void showBottomSheetDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_dialog, null);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(view);
        //给布局设置透明背景色
        bottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet)
                .setBackgroundColor(getResources().getColor(android.R.color.transparent));
        bottomSheetDialog.show();

        view.findViewById(R.id.bottom_image1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityDiaryWrite.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
        view.findViewById(R.id.bottom_image2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ActivityNoteWrite.class);
                startActivity(intent);
                bottomSheetDialog.dismiss();
            }
        });
    }

    /**
     * 点击菜单
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Glide.with(MainActivity.this)
                .load(UserUtil.user.getUserHeaderPic())
                .error(R.drawable.touxiang)
                .dontAnimate()
                .into(headerPic);

    }

    /**
     * 个人中心的点击
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
            Intent intent = new Intent(MainActivity.this,ActivitySetHeaderPic.class);
            startActivity(intent);
        } else if (id == R.id.nav_gallery) {
            Intent intent = new Intent(MainActivity.this,ActivityUserMessage.class);
            startActivity(intent);
        } else if (id == R.id.nav_slideshow) {
            Intent intent = new Intent(MainActivity.this,ActivityUserDiary.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent = new Intent(MainActivity.this,ActivityCollection.class);
            startActivity(intent);
        } else if (id == R.id.nav_share) {
            shareAplcation();
        } else if (id == R.id.nav_send) {
            BmobUser.logOut();
            // DataSupport.deleteAll(User.class,"userId=?",user.getObjectId());
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * view的点击
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.home_diary2: {
                setBackgroud(1);
                replace(new DiaryFragment());
                titleText.setText(R.string.bottom_text1);
                break;
            }
            case R.id.home_note2: {
                setBackgroud(2);
                replace(new NoteFragment());
                titleText.setText(R.string.bottom_text2);
                break;
            }
            case R.id.home_article2: {
                setBackgroud(3);
                replace(new ArticleFragment());
                titleText.setText(R.string.bottom_text3);
                break;
            }
            case R.id.fab:{
                ObjectAnimator rotation=ObjectAnimator.ofFloat(fab,"rotation",360,0);
                rotation.setDuration(1500);
                rotation.setInterpolator(new BounceInterpolator());
                rotation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        showBottomSheetDialog();
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

    /**
     * 设置背景
     * @param tag
     */
    public void setBackgroud(int tag) {
        diary.setImageResource(R.drawable.riji);
        note.setImageResource(R.drawable.bianqian);
        article.setImageResource(R.drawable.wenzhang);
        textDiary.setTextColor(Color.parseColor("#2b2b2b"));
        textNote.setTextColor(Color.parseColor("#2b2b2b"));
        textArticle.setTextColor(Color.parseColor("#2b2b2b"));
        if (tag == 1) {
            diary.setImageResource(R.drawable.riji2);
            textDiary.setTextColor(Color.parseColor("#5299f5"));
        } else if (tag == 2) {
            note.setImageResource(R.drawable.bianqian2);
            textNote.setTextColor(Color.parseColor("#5299f5"));
        } else if (tag == 3) {
            article.setImageResource(R.drawable.wenzhang2);
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


    public void shareAplcation(){
        final String[] items = new String[] { "微信朋友圈","微信群聊","QQ群聊","QQ空间"};
        // 创建对话框构建器
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        // 设置参数
        builder.setIcon(R.drawable.fenxiang).setTitle("推荐你的朋友使用Note")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which==0){
                            shareText("请到www.note.com下载使用该软件",0);
                        }else if(which==1){
                            shareText("请到www.note.com下载使用该软件",1);
                        }else if(which==2){
                            shareToQQ("请到www.note.com下载使用该软件");
                        }else if(which==3){
                           shareToQZone("请到www.note.com下载使用该软件");
                        }
                    }
                });
        builder.create().show();
    }



    /**
     * qq分享
     * @param text
     */
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

    /**
     * 空间分享
     * @param text
     */
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

    /**
     * 设置悬浮按钮的显示隐藏
     * @param tag
     */
    @SuppressLint("RestrictedApi")
    public void hinden(int tag) {
        if (tag == 1) {
            fab.setVisibility(View.VISIBLE);
        } else if (tag == 2) {
            fab.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void start() {
        dialog.show();
    }

    @Override
    public void sucessful() {
        replace(new DiaryFragment());
    }

    @Override
    public void cancle() {
        dialog.dismiss();
    }

    @Override
    public void error() {
        Toast.makeText(MainActivity.this,"数据加载错误",Toast.LENGTH_SHORT).show();
    }

}
