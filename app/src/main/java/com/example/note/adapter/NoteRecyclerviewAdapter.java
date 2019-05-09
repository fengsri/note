package com.example.note.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.note.NoteShowActivity;
import com.example.note.R;
import com.example.note.domain.Diary;
import com.example.note.domain.Note;

import java.util.List;

/**
 * Created by asus on 2019/2/15.
 */

public class NoteRecyclerviewAdapter extends RecyclerView.Adapter<NoteRecyclerviewAdapter.ViewHolder> {
    private List<Note> noteList;
    private Context context;

    public NoteRecyclerviewAdapter(List<Note> noteList) {
        this.noteList = noteList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_note_recyclerview_items,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Note note = noteList.get(position);

        note.setTitle("这是便签标题");
        note.setText("这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容这是便签内容");
        note.setDate("2019/05/09");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, NoteShowActivity.class);
                intent.putExtra("title",note.getTitle());
                intent.putExtra("text",note.getText());
                intent.putExtra("date",note.getDate());
                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout item;
        TextView title;
        TextView date;
        TextView smalltext;
        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.note_item);
            title=itemView.findViewById(R.id.note_item_title);
            date=itemView.findViewById(R.id.note_item_date);
            smalltext=itemView.findViewById(R.id.note_item_smalltext);
        }
    }

}
