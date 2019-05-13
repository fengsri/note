package com.example.note.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Article extends DataSupport {
    private String objectId;
    private String date;
    private String title;
    private String text;
    private String icon;
    private String pic1;
    private String pic2;
    private String pic3;
    private String type;

    public Article() {
    }

    public Article(String objectId, String date, String title, String text, String icon, String pic1, String pic2, String pic3, String type) {
        this.objectId = objectId;
        this.date = date;
        this.title = title;
        this.text = text;
        this.icon = icon;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.pic3 = pic3;
        this.type = type;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getPic3() {
        return pic3;
    }

    public void setPic3(String pic3) {
        this.pic3 = pic3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
