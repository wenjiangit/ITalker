package com.example.factory.net.rx;

import com.example.commom.net.model.RegisterModel;
import com.example.factory.model.api.AccountRspModel;
import com.example.factory.model.api.GroupCreateModel;
import com.example.factory.model.api.GroupMemberAddModel;
import com.example.factory.model.api.LoginModel;
import com.example.factory.model.api.MessageCreateModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.api.UpdateInfoModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * 网络请求接口
 *
 * Created by wenjian on 2017/6/14.
 */

public interface RxRemoteService {

    @POST("account/register")
    Observable<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    @POST("account/login")
    Observable<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    @POST("account/bind/{pushId}")
    Observable<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    @PUT("user")
    Observable<RspModel<UserCard>> userUpdate(@Body UpdateInfoModel model);

    @GET("user/search/{name}")
    Observable<RspModel<List<UserCard>>> userSearch(@Path(encoded = true, value = "name") String name);

    @PUT("user/follow/{userId}")
    Observable<RspModel<UserCard>> userFollow(@Path("userId") String userId);

    @GET("user/contact")
    Observable<RspModel<List<UserCard>>> userContact();

    @GET("user/{userId}")
    Observable<RspModel<UserCard>> userFind(@Path("userId") String userId);

    @POST("msg")
    Observable<RspModel<MessageCard>> msgPush(@Body MessageCreateModel model);

    //群创建
    @POST("group")
    Observable<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    //我的群
    @GET("group/list/{date}")
    Observable<RspModel<List<GroupCard>>> groupList(@Path(value = "date", encoded = true) String dateStr);

    //查询一个群
    @GET("group/{groupId}")
    Observable<RspModel<GroupCard>> getGroup(@Path("groupId") String id);

    //群搜索
    @GET("group/search/{name}")
    Observable<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    //群成员
    @GET("group/{groupId}/member")
    Observable<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    //群成员
    @GET("group/{groupId}/member")
    Observable<RspModel<List<GroupMemberCard>>> members(@Path("groupId") String groupId);

    //添加群成员
    @POST("group/{groupId}/member")
    Observable<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);










}
