package com.example.note.domain;

import org.litepal.crud.DataSupport;

import cn.bmob.v3.BmobUser;

public class User  extends DataSupport {
    private String userId;
    private String userUseDate;
    private String userSignature;
    private String userHeaderPic;
    private String userSchool;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserUseDate() {
        return userUseDate;
    }

    public void setUserUseDate(String userUseDate) {
        this.userUseDate = userUseDate;
    }

    public String getUserSignature() {
        return userSignature;
    }

    public void setUserSignature(String userSignature) {
        this.userSignature = userSignature;
    }

    public String getUserHeaderPic() {
        return userHeaderPic;
    }

    public void setUserHeaderPic(String userHeaderPic) {
        this.userHeaderPic = userHeaderPic;
    }

    public String getUserSchool() {
        return userSchool;
    }

    public void setUserSchool(String userSchool) {
        this.userSchool = userSchool;
    }

}
