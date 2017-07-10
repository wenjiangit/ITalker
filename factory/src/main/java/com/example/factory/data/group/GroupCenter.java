package com.example.factory.data.group;

import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;

/**
 *
 * Created by douliu on 2017/6/27.
 */

public interface GroupCenter {

    void dispatch(GroupCard... cards);

    void dispatch(GroupMemberCard... cards);
}
