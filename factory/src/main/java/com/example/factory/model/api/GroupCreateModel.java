package com.example.factory.model.api;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by douliu on 2017/7/18.
 */

public class GroupCreateModel {
    private String name;
    private String desc;
    private String picture;
    private Set<String> memberIds = new HashSet<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Set<String> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(Set<String> memberIds) {
        this.memberIds = memberIds;
    }
}
