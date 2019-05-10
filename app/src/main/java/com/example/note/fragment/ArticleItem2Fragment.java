package com.example.note.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.ArticleRecyclerviewAdapter;
import com.example.note.dao.ArticleDao;
import com.example.note.domain.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleItem2Fragment extends Fragment implements View.OnClickListener {
    private Context context;
    private RecyclerView recyclerView;
    public List<Article> articleList = new ArrayList<>();
    public ArticleRecyclerviewAdapter adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取本地数据库的数据
        initData();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = null;
        if(articleList!=null && articleList.size()>0){
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_article_item2,container,false);
            ((MainActivity)context).hinden(2);
            //initData();
            init(view);
        }else{
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.data_false,container,false);
        }
        return view;
    }

    public void initData() {
        articleList = ArticleDao.getByTypeArticleFromLitePal("2",articleList);
    }

    public void init(View view) {
        recyclerView = view.findViewById(R.id.article_item2_recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        adapter = new ArticleRecyclerviewAdapter(articleList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}

