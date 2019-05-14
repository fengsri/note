package com.example.note.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Collection extends BmobObject implements Serializable {
    private User user;
    private Article article;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
