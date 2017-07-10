package com.example.factory.data.user;

import com.example.factory.model.card.UserCard;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public interface UserCenter {

    void dispatch(UserCard... userCards);

}
