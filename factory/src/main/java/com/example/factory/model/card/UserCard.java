package com.example.factory.model.card;

import com.example.commom.factory.model.Author;
import com.example.factory.model.db.User;

import java.util.Date;

/**
 * 用户卡片
 * Created by wenjian on 2017/6/18.
 */

public class UserCard implements Author{

    private String id;

    private String name;

    private String phone;

    private String portrait;

    private String desc;

    private int sex = 0;

    private int followers;//粉丝的个数

    private int followings;//我关注的人个数

    private boolean isFollow;

    private Date modifyAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowings() {
        return followings;
    }

    public void setFollowings(int followings) {
        this.followings = followings;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public Date getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Date modifyAt) {
        this.modifyAt = modifyAt;
    }

    private User user;

    public User buildUser() {
        if (user == null) {
            user = new User();
            user.setId(id);
            user.setDesc(desc);
            user.setFollow(isFollow);
            user.setFollowing(followings);
            user.setFollows(followers);
            user.setModifyAt(modifyAt);
            user.setName(name);
            user.setPhone(phone);
            user.setPortrait(portrait);
            user.setSex(sex);
        }
        return user;
    }


    @Override
    public String toString() {
        return "UserCard{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                ", followers=" + followers +
                ", followings=" + followings +
                ", isFollow=" + isFollow +
                ", modifyAt=" + modifyAt +
                '}';
    }
}
