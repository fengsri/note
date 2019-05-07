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

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.adapter.NoteRecyclerviewAdapter;
import com.example.note.domain.Note;

import java.util.ArrayList;
import java.util.List;

public class NoteFragment extends Fragment implements View.OnClickListener{

    private Context context;
    private RecyclerView recyclerView;
    private List<Note> noteList = new ArrayList<>();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        View view =LayoutInflater.from(container.getContext()).inflate(R.layout.fragment_note,container,false);
        ((MainActivity)context).hinden(1);
        initData();
        init(view);
        return view;
    }

    private void initData() {
        for(int i=0;i<20;i++){
            Note note =new Note();
            noteList.add(note);
        }
    }

    public void init(View view){
        recyclerView = view.findViewById(R.id.note_recyclerView);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        NoteRecyclerviewAdapter adapter = new NoteRecyclerviewAdapter(noteList);
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

