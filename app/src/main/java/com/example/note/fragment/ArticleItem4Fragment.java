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

import com.example.note.R;
import com.example.note.adapter.ArticleRecyclerviewAdapter;
import com.example.note.domain.Article;

import java.util.ArrayList;
import java.util.List;

public class ArticleItem4Fragment extends Fragment implements View.OnClickListener{
    private Context context;
    private RecyclerView recyclerView;
    private List<Article> articleList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_article_item4,container,false);
        initData();
        init(view);
        return view;
    }

    private void initData() {
        for(int i=0;i<20;i++){
            Article article = new Article();
            articleList.add(article);
        }
    }

    public void init(View view){
        recyclerView  = view.findViewById(R.id.article_item4_recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        ArticleRecyclerviewAdapter adapter = new ArticleRecyclerviewAdapter(articleList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }
    }

}

