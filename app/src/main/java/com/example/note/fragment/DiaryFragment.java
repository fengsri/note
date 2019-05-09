package com.example.note.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.DairyRecyclerviewAdapter;
import com.example.note.dao.DiaryDao;
import com.example.note.domain.Diary;
import com.example.note.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class DiaryFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private RecyclerView recyclerView;
    private List<Diary> diaryList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //获取本地数据库的数据
        diaryList = DiaryDao.getDiaryFromLitePal(UserUtil.user.getObjectId());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view = null;
        if(diaryList!=null && diaryList.size()>0){
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_diary,container,false);
            ((MainActivity)context).hinden(1);
            //initData();
            init(view);
        }else{
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.data_null,container,false);
        }
        return view;
    }

    private void initData() {
        diaryList = DiaryDao.getDiaryFromLitePal(UserUtil.user.getObjectId());
    }

    public void init(View view){
        recyclerView = view.findViewById(R.id.diay_recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(context);
        DairyRecyclerviewAdapter adapter=new DairyRecyclerviewAdapter(diaryList);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        registerForContextMenu(recyclerView);
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

