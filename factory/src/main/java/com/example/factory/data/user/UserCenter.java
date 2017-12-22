package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

/**
 * 用户中心
 *
 * @author douliu
 * @date 2017/6/27
 */

public interface UserCenter {

    void dispatch(UserCard... userCards);

}
