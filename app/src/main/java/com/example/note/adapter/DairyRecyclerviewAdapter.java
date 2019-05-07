package com.example.note.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.domain.Article;
import com.example.note.domain.Diary;

import java.util.List;

/**
 * Created by asus on 2019/2/15.
 */

public class DairyRecyclerviewAdapter extends RecyclerView.Adapter<DairyRecyclerviewAdapter.ViewHolder> {
    private List<Diary> diaryList;
    private Context context;

    public DairyRecyclerviewAdapter(List<Diary> diaryList) {
        this.diaryList = diaryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_diary_recyclerview_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Diary diary = diaryList.get(position);
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
        return diaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView day;
        TextView month;
        TextView week;
        TextView weather;


        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.recyclerview_diary_item_image);
            day=itemView.findViewById(R.id.recyclerview_diary_item_day);
            month=itemView.findViewById(R.id.recyclerview_diary_item_month);
            week=itemView.findViewById(R.id.recyclerview_diary_item_week);
            weather=itemView.findViewById(R.id.recyclerview_diary_item_weather);
        }
    }

}
