package com.example.note.bean;

import cn.bmob.v3.BmobUser;

public class User extends BmobUser {
    private String userUseDate;
    private String userSignature;
    private String userHeaderPic;
    private String userSchool;

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
