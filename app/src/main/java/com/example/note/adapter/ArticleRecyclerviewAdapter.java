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

import com.bumptech.glide.Glide;
import com.example.note.ArticleShowActivity;
import com.example.note.MainActivity;
import com.example.note.R;
import com.example.note.domain.Article;
import com.example.note.util.ImageUtil;
import com.example.note.util.StringUtil;

import java.util.List;

/**
 * Created by asus on 2019/2/15.
 */

public class ArticleRecyclerviewAdapter extends RecyclerView.Adapter<ArticleRecyclerviewAdapter.ViewHolder> {
    private List<Article> articleList;
    private Context context;

    public ArticleRecyclerviewAdapter(List<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context=parent.getContext();
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_article_recyclerview_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Article article2 = articleList.get(position);
        holder.titleView.setText(article2.getTitle());
        holder.dataText.setText(article2.getDate());
        holder.countText.setText((int)(Math.random()*10)+"万人浏览");
        int error = ImageUtil.getRandImageId();
        Glide.with(context)
                .load(article2.getIcon())
                .error(error)
                .placeholder(error)
                .dontAnimate()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ArticleShowActivity.class);
                intent.putExtra("title",article2.getTitle());
                intent.putExtra("text",article2.getText());
                intent.putExtra("auther", StringUtil.getRandString());
                intent.putExtra("id", article2.getObjectId());
                context.startActivity(intent);
            }
        });
        holder.shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] { "微信朋友圈","微信群聊","QQ群聊","QQ空间"};
                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置参数
                builder.setIcon(R.drawable.fenxiang).setTitle("分享")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    ((MainActivity)(context)).shareText("美文："+ article2.getTitle(),0);
                                }else if(which==1){
                                    ((MainActivity)(context)).shareText("美文："+ article2.getTitle(),1);
                                }else if(which==2){
                                    ((MainActivity)(context)).shareToQQ("美文："+ article2.getTitle());
                                }else if(which==3){
                                    ((MainActivity)(context)).shareToQZone("美文："+ article2.getTitle());
                                }
                            }
                        });
                builder.create().show();
            }
        });
        holder.shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = new String[] { "微信朋友圈","微信群聊","QQ群聊","QQ空间"};
                // 创建对话框构建器
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                // 设置参数
                builder.setIcon(R.drawable.fenxiang).setTitle("分享")
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(which==0){
                                    ((MainActivity)(context)).shareText("美文："+ article2.getTitle(),0);
                                }else if(which==1){
                                    ((MainActivity)(context)).shareText("美文："+ article2.getTitle(),1);
                                }else if(which==2){
                                    ((MainActivity)(context)).shareToQQ("美文："+ article2.getTitle());
                                }else if(which==3){
                                    ((MainActivity)(context)).shareToQZone("美文："+ article2.getTitle());
                                }
                            }
                        });
                builder.create().show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView titleView;
        TextView dataText;
        TextView countText;
        ImageView shareImage;
        TextView shareText;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.article_item_image);
            titleView=itemView.findViewById(R.id.article_item_title);
            dataText=itemView.findViewById(R.id.article_item_date);
            countText = itemView.findViewById(R.id.practice_item_count);
            shareImage=itemView.findViewById(R.id.practice_item_fximage);
            shareText=itemView.findViewById(R.id.practice_item_sharetext);
        }
    }

}
