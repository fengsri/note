package com.example.note.mvp.model;


import com.example.note.bean.Article;
import com.example.note.dao.ArticleDao;
import com.example.note.mvp.listener.MyListener;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class ArticleModel {

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
     */
    public static void saveBeanArticleToYun(String date,String title,String text,String icon,String pic1,String pic2,String pic3,String type,  final MyListener listener){
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
            public void done(String objectId, BmobException e) {
                if(e==null){
                    listener.sucess("成功");
                }else{
                    listener.error();
                }
            }
        });
    }


    /**
     * 刷新数据
     */
    public static void refreshNewArticle(final MyListener listener){
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
                    listener.sucess("成功");
                }
                else {
                    listener.error();
                }
            }
        });
    }


}
