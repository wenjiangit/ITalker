package com.example.factory.model.api;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * Created by wenjian on 2017/7/23.
 */

public class GroupMemberAddModel {

    private Set<String> users = new HashSet<>();

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
