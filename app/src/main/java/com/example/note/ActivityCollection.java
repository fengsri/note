package com.example.note;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.note.adapter.ArticleRecyclerviewAdapter;
import com.example.note.adapter.CommentRecyclerviewAdapter;
import com.example.note.bean.Collection;
import com.example.note.bean.Comment;
import com.example.note.domain.Article;
import com.example.note.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ActivityCollection extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Article> articleList = new ArrayList<>();
    private ArticleRecyclerviewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoucang);
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setHomeButtonEnabled(true);//主键按钮能否可点击
        supportActionBar.setDisplayHomeAsUpEnabled(true);//显示返回图标
        this.setTitle("收藏");

        recyclerView = findViewById(R.id.collection_recyclerview);

        BmobQuery<Collection> query = new BmobQuery<Collection>();
//用此方式可以构造一个BmobPointer对象。只需要设置objectId就行
        query.addWhereEqualTo("user", new BmobPointer(UserUtil.user));
//希望同时查询该评论的发布者的信息，以及该帖子的作者的信息，这里用到上面`include`的并列对象查询和内嵌对象的查询
        query.include("article");
        query.findObjects(new FindListener<Collection>() {
            @Override
            public void done(List<Collection> objects, BmobException e) {
                if (objects!=null && objects.size()>0) {
                    for(Collection collection:objects) {
                        com.example.note.bean.Article article = collection.getArticle();
                        Article tag = new Article();
                        tag.setObjectId(article.getObjectId());
                        tag.setType(article.getType());
                        tag.setDate(article.getDate());
                        tag.setTitle(article.getTitle());
                        tag.setText(article.getText());
                        tag.setPic1(article.getPic1());
                        articleList.add(tag);
                    }
                }
                LinearLayoutManager manager = new LinearLayoutManager(ActivityCollection.this);
                adapter = new ArticleRecyclerviewAdapter(articleList,1);
                recyclerView.setLayoutManager(manager);
                recyclerView.setAdapter(adapter);
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
