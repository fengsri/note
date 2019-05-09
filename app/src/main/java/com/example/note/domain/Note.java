package com.example.note.domain;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

public class Note extends DataSupport {
    private String objectId;
    private String userId;
    private String date;
    private String title;
    private String text;
    private String pic;
    private String icon;
    private String address;
    private String weather;

    public Note() {
    }

    public Note(String objectId, String userId, String date, String title, String text, String pic, String icon, String address, String weather) {
        this.objectId = objectId;
        this.userId = userId;
        this.date = date;
        this.title = title;
        this.text = text;
        this.pic = pic;
        this.icon = icon;
        this.address = address;
        this.weather = weather;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }
}
