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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.note.ActivitySetHeaderPic;
import com.example.note.ArticleShowActivity;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.bean.Comment;
import com.example.note.domain.Article;
import com.example.note.util.ImageUtil;
import com.example.note.util.UserUtil;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by asus on 2019/2/15.
 */

public class CommentRecyclerviewAdapter extends RecyclerView.Adapter<CommentRecyclerviewAdapter.ViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentRecyclerviewAdapter(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_recyclerview_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final Comment comment = commentList.get(position);
        holder.name.setText(comment.getUser().getUsername());
        holder.text.setText(comment.getComment());
        holder.count.setText(comment.getCount()+"");
        int error = ImageUtil.getRandImageId();
        Glide.with(context)
                .load(comment.getUser().getUserHeaderPic())
                .error(error)
                .placeholder(error)
                .dontAnimate()
                .into(holder.headerpic);
        holder.countImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comment.setCount(comment.getCount()+1);
                holder.count.setText(comment.getCount()+1+"");
                comment.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.makeText(context, "成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
       private CircleImageView headerpic;
       private TextView name;
       private TextView text;
       private ImageView countImage;
       private TextView count;
        public ViewHolder(View itemView) {
            super(itemView);
            headerpic=itemView.findViewById(R.id.comment_header_pic);
            name = itemView.findViewById(R.id.comment_name);
            text = itemView.findViewById(R.id.comment_text);
            countImage = itemView.findViewById(R.id.comment_count_image);
            count = itemView.findViewById(R.id.comment_count);
        }
    }

}
