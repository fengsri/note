package com.example.note.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.note.DiaryShowActivity;
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_diary_recyclerview_items,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Diary diary = diaryList.get(position);

        final Diary diary2 = new Diary();
        diary2.setDate("2019/05/09");
        diary2.setAddress("成都市西华大学");
        diary2.setIcon("");
        diary2.setPic("");
        diary2.setText("大萨达撒多撒多撒大萨达撒多撒敖德萨多撒阿达阿斯顿撒多阿斯顿撒多撒阿大风吹散发飒飒");
        diary2.setWeather("晴天");
        diary2.setTitle("这是日记的标题");
        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, DiaryShowActivity.class);
                intent.putExtra("date",diary2.getDate());
                intent.putExtra("title",diary2.getTitle());
                intent.putExtra("text",diary2.getText());
                intent.putExtra("pic",diary2.getPic());
                intent.putExtra("address",diary2.getAddress());
                intent.putExtra("weather",diary2.getWeather());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout item;
        TextView title;
        TextView date;
        ImageView imageView;
        TextView weather;
        TextView address;

        public ViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.diary_item);
            title  =itemView.findViewById(R.id.diary_item_title);
            date=itemView.findViewById(R.id.diary_item_date);
            imageView =itemView.findViewById(R.id.diary_item_image);
            weather=itemView.findViewById(R.id.dairy_item_weather_text);
            address=itemView.findViewById(R.id.dairy_item_address_text);
        }
    }

}
