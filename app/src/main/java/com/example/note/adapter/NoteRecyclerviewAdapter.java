package com.example.note.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_note_recyclerview_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Note note = noteList.get(position);
//        Glide.with(context)
//                    .load(article.getIcon())
//                    .error(R.drawable.luncher_bg2)
//                    .dontAnimate()
//                    .into(holder.imageView);

//        holder.imageView.setImageResource(R.drawable.luncher_bg5);
//        holder.textView.setText(article.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(context, PracticeActivity.class);
//                context.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recyclerview_note_item_image);
            title=itemView.findViewById(R.id.recyclerview_note_item_title);
        }
    }

}
