package com.example.factory.model.api;

/**
 *
 *
 * Created by wenjian on 2017/6/18.
 */

public class UpdateInfoModel {

    private String name;

    private String portrait;

    private String desc;

    private int sex;

    public UpdateInfoModel(String portrait, String desc, int sex) {
        this.portrait = portrait;
        this.desc = desc;
        this.sex = sex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return "UpdateInfoModel{" +
                "name='" + name + '\'' +
                ", portrait='" + portrait + '\'' +
                ", desc='" + desc + '\'' +
                ", sex=" + sex +
                '}';
    }
}
