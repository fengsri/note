package com.example.note.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyz.widget.PullRefreshLayout;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.NoteRecyclerviewAdapter;
import com.example.note.dao.NoteDao;
import com.example.note.domain.Note;
import com.example.note.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private RecyclerView recyclerView;
    private List<Note> noteList = new ArrayList<>();
    private NoteRecyclerviewAdapter adapter;
    private PullRefreshLayout pullRefreshLayout;
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
        if(noteList!=null && noteList.size()>0){
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_note,container,false);
            ((MainActivity)context).hinden(1);
            // initData();
            init(view);
        }else{
            view =LayoutInflater.from(container.getContext()).inflate(R.layout.data_null,container,false);
            ((MainActivity)context).hinden(1);
        }
        return view;
    }

    private void initData() {
        noteList = NoteDao.getNoteFromLitePal(UserUtil.user.getObjectId(),noteList);
    }

    public void init(View view){
        pullRefreshLayout = view.findViewById(R.id.note_swipeRefreshLayout);
        recyclerView = view.findViewById(R.id.note_recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter = new NoteRecyclerviewAdapter(noteList);
        recyclerView.setAdapter(adapter);
        pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        shuaxing();
                        // 刷新3秒完成
                        pullRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    public void shuaxing(){
        initData();
        if(noteList!=null && noteList.size()>0){
            adapter.notifyDataSetChanged();
        }
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

