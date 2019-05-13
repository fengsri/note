package com.example.note;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.note.adapter.ArticleRecyclerviewAdapter;
import com.example.note.adapter.CommentRecyclerviewAdapter;
import com.example.note.adapter.ImagerViewPager;
import com.example.note.bean.Article;
import com.example.note.bean.Comment;
import com.example.note.util.ImageUtil;
import com.example.note.myview.CircleIndicator2;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ArticleShowActivity extends AppCompatActivity {

    private TextView title;
    private TextView auther;
    private TextView text;
    private ViewPager viewPager;
    private CircleIndicator2 indicator2;
    private RecyclerView commentRecyclerView;
    private List<Comment> commentList;


    private String objectId;
    private String titlestr;
    private String autherstr;
    private String textstr;

    private List<ImageView> list = new ArrayList<ImageView>();
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
        ImagerViewPager imagerViewPager=new ImagerViewPager(list);
        viewPager.setAdapter(imagerViewPager);
        indicator2.setUpWithViewPager(viewPager);
        this.setTitle(titlestr);

        commentRecyclerView = findViewById(R.id.article_show_comment);
    }


    private void initData() {
          titlestr = getIntent().getStringExtra("title");
          autherstr= getIntent().getStringExtra("auther");
          textstr= getIntent().getStringExtra("text");
         objectId= getIntent().getStringExtra("id");

         for(int i=0;i<3;i++){
             ImageView imageView=new ImageView(ArticleShowActivity.this);
             imageView.setScaleType(ImageView.ScaleType.FIT_XY);
             imageView.setImageResource(ImageUtil.getRandImageId());
             imageView.setTag(i);
             list.add(imageView);
         }

         BmobQuery<Comment> query = new BmobQuery<Comment>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        Article article = new Article();
        article.setObjectId(objectId);
        query.addWhereEqualTo("article",new BmobPointer(article));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> objects, BmobException e) {
                commentList =objects;
                if(commentList!=null && commentList.size()>0){
                    LinearLayoutManager manager = new LinearLayoutManager(ArticleShowActivity.this);
                    CommentRecyclerviewAdapter adapter = new CommentRecyclerviewAdapter(commentList);
                    commentRecyclerView.setLayoutManager(manager);
                    commentRecyclerView.setAdapter(adapter);
                }
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

}



