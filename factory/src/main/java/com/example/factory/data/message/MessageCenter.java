package com.example.factory.data.message;

import com.example.factory.model.card.MessageCard;

/**
 * 消息中心
 *
 * Created by douliu on 2017/6/27.
 */

public interface MessageCenter {



    void dispatch(MessageCard... cards);

}
