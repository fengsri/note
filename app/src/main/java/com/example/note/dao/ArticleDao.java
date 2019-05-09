package com.example.note.dao;

import android.content.Context;
import android.widget.Toast;

import com.example.note.bean.Article;
import com.example.note.bean.Diary;
import com.example.note.bean.Note;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ArticleDao {

    /**
     * 保存数据到本地
     * @param article
     */
    public static void saveDiaryBeanToLitPal(Article article){
        com.example.note.domain.Article articleS = new com.example.note.domain.Article();
        articleS.setDate(article.getDate());
        articleS.setTitle(article.getTitle());
        articleS.setText(article.getText());
        articleS.setIcon(article.getIcon());
        articleS.setPic1(article.getPic1());
        articleS.setPic2(article.getPic2());
        articleS.setPic3(article.getPic3());
        articleS.setType(article.getType());
        articleS.save();
    }


    /**
     * 保存数据到云
     * @param date
     * @param title
     * @param text
     * @param icon
     * @param pic1
     * @param pic2
     * @param pic3
     * @param type
     * @param context
     */
    public static void saveBeanArticleToYun(String date,String title,String text,String icon,String pic1,String pic2,String pic3,String type, final Context context){
        Article article = new Article();
        article.setDate(date);
        article.setTitle(title);
        article.setText(text);
        article.setIcon(icon);
        article.setPic1(pic1);
        article.setPic2(pic2);
        article.setPic3(pic3);
        article.setType(type);
        article.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    Toast.makeText(context,"添加数据成功",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(context,"创建数据失败",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    /**
     * 刷新数据
     */
    public static void refreshNewArticle(){
        BmobQuery<Article> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> beanArticle, BmobException e) {
                if (e == null) {
                    ArticleDao.deleteDiaryFromLitePal();
                    if (beanArticle != null && beanArticle.size()>0) {
                        for (Article article : beanArticle) {
                            ArticleDao.saveDiaryBeanToLitPal(article);
                        }
                    }
                }
            }
        });
    }

    /**
     * 通过类型获取本地数据
     * @param type
     * @return
     */
    public static List<com.example.note.domain.Article> getByTypeArticleFromLitePal(String type){
        List<com.example.note.domain.Article> articleList = ArticleDao.getArticleFromLitePal();
        List<com.example.note.domain.Article> data = new ArrayList<com.example.note.domain.Article>();
        for(com.example.note.domain.Article article:articleList){
            String typestr = article.getType();
            if (type.equals(typestr)) {
                data.add(article);
            }
        }
        return data;
    }

    /**
     * 获取本地所有数据
     * @return
     */
    public static List<com.example.note.domain.Article> getArticleFromLitePal(){
        List<com.example.note.domain.Article> articleList = DataSupport.findAll(com.example.note.domain.Article.class);
        return articleList;
    }

    /**
     * 删除本地数据
     */
    public static void deleteDiaryFromLitePal(){
        DataSupport.deleteAll(com.example.note.domain.Article.class);
    }

}
