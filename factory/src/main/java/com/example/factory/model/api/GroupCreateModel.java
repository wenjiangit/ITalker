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
    private Set<String> members = new HashSet<>();

    public GroupCreateModel(String name, String desc, String picture, Set<String> memberIds) {
        this.name = name;
        this.desc = desc;
        this.picture = picture;
        this.members = memberIds;
    }

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
        return members;
    }

    public void setMemberIds(Set<String> memberIds) {
        this.members = memberIds;
    }
}
