package com.example.note;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.adapter.ArticleRecyclerviewAdapter;
import com.example.note.adapter.CommentRecyclerviewAdapter;
import com.example.note.adapter.ImagerViewPager;
import com.example.note.bean.Article;
import com.example.note.bean.Collection;
import com.example.note.bean.Comment;
import com.example.note.bean.User;
import com.example.note.util.ImageUtil;
import com.example.note.myview.CircleIndicator2;
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

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ArticleShowActivity extends AppCompatActivity {

    private TextView title;
    private TextView auther;
    private TextView text;
    private ViewPager viewPager;
    private CircleIndicator2 indicator2;
    private RecyclerView commentRecyclerView;
    private List<Comment> commentList = new ArrayList<Comment>();
    private CommentRecyclerviewAdapter adapter;

    private TextView commentText;
    private TextView pinglunText;
    private ImageView fengxiang;
    private ImageView shoucang;

    private String objectId;
    private String titlestr;
    private String autherstr;
    private String textstr;

    private List<ImageView> list = new ArrayList<ImageView>();
    private IWXAPI api;
    private Tencent mTencent;
    private ArticleShowActivity.BaseUiListener mIUiListener = new ArticleShowActivity.BaseUiListener();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_show);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标

        initData();
        initView();
        //第一：默认初始化微信
        api = WXAPIFactory.createWXAPI(this, "wx31255991882d79b5");
        api.registerApp("wx31255991882d79b5");
        //qq的默认
        mTencent = Tencent.createInstance("101572837", this.getApplicationContext());
    }


    private void initView() {
        title = findViewById(R.id.article_show_title);
        title.setText(titlestr);
        auther = findViewById(R.id.article_show_auther);
        auther.setText(autherstr);
        text = findViewById(R.id.article_show_text);
        text.setText(textstr);

        indicator2 = findViewById(R.id.article_header_indicator_show);
        viewPager = findViewById(R.id.article_header_show);
        ImagerViewPager imagerViewPager = new ImagerViewPager(list);
        viewPager.setAdapter(imagerViewPager);
        indicator2.setUpWithViewPager(viewPager);
        this.setTitle(titlestr);

        commentRecyclerView = findViewById(R.id.article_show_comment);
        commentText = findViewById(R.id.comment_write_content);
        pinglunText = findViewById(R.id.comment_write_pinglun);
        fengxiang = findViewById(R.id.comment_write_fengxiang);
        shoucang = findViewById(R.id.comment_write_shoucahgn);
        shoucang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = BmobUser.getCurrentUser(User.class);
                Article article = new Article();
                article.setObjectId(objectId);
                final Collection collection = new Collection();
                collection.setArticle(article);
                collection.setUser(user);
                collection.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Toast.makeText(ArticleShowActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ArticleShowActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        fengxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String[] items = new String[] { "微信朋友圈","微信群聊","QQ群聊","QQ空间"};
                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(ArticleShowActivity.this);
                // 设置参数
                builder.setIcon(R.drawable.fenxiang).setTitle("分享")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    shareText("美文："+ titlestr,0);
                                }else if(which==1){
                                    shareText("美文："+ titlestr,1);
                                }else if(which==2){
                                    shareToQQ("美文："+ titlestr);
                                }else if(which==3){
                                    shareToQZone("美文："+ titlestr);
                                }
                            }
                        });
                builder.create().show();
            }
        });
        pinglunText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = commentText.getText().toString();
                User user = BmobUser.getCurrentUser(User.class);
                Article article = new Article();
                article.setObjectId(objectId);
                final Comment comment = new Comment();
                comment.setComment(text);
                comment.setArticle(article);
                comment.setUser(user);
                comment.save(new SaveListener<String>() {
                    @Override
                    public void done(String objectId, BmobException e) {
                        if (e == null) {
                            Toast.makeText(ArticleShowActivity.this, "评论发表成功", Toast.LENGTH_SHORT).show();
                            commentList.add(comment);
                            adapter.notifyDataSetChanged();
                            commentText.setText("");
                        } else {
                            commentText.setText("");
                            Toast.makeText(ArticleShowActivity.this, "评论发表失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }


    private void initData() {
        titlestr = getIntent().getStringExtra("title");
        autherstr = getIntent().getStringExtra("auther");
        textstr = getIntent().getStringExtra("text");
        objectId = getIntent().getStringExtra("id");

        for (int i = 0; i < 3; i++) {
            ImageView imageView = new ImageView(ArticleShowActivity.this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(ImageUtil.getRandImageId());
            imageView.setTag(i);
            list.add(imageView);
        }

        BmobQuery<Comment> query = new BmobQuery<Comment>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        Article article = new Article();
        article.setObjectId(objectId);
        query.addWhereEqualTo("article", new BmobPointer(article));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> objects, BmobException e) {
                if (objects!=null && objects.size()>0) {
                    commentList = objects;
                }
                LinearLayoutManager manager = new LinearLayoutManager(ArticleShowActivity.this);
                adapter = new CommentRecyclerviewAdapter(commentList);
                commentRecyclerView.setLayoutManager(manager);
                commentRecyclerView.setAdapter(adapter);
            }
        });
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
                mTencent.shareToQQ(ArticleShowActivity.this, params, new ArticleShowActivity.BaseUiListener());
            }
        });
    }

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



