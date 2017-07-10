package com.example.factory.presenter.message;

import android.support.v7.util.DiffUtil;

import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.factory.data.helper.MessageHelper;
import com.example.factory.data.message.MessageDataSource;
import com.example.factory.model.api.MessageCreateModel;
import com.example.factory.model.db.Message;
import com.example.factory.persistant.Account;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/8.
 */

public class ChatPresenter<Model> extends BaseSourcePresenter<Message, Message,MessageDataSource, ChatContract.View<Model>>
        implements ChatContract.Presenter {

    protected String mReceiverId;
    protected int mReceiverType;

    public ChatPresenter(MessageDataSource source, ChatContract.View<Model> view,
                         String receiverId,int receiverType) {
        super(source, view);
        this.mReceiverId = receiverId;
        this.mReceiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        MessageCreateModel model = new MessageCreateModel.Builder()
                .receiver(mReceiverId, mReceiverType)
                .content(content, Message.TYPE_STR)
                .build();
        MessageHelper.push(model);
    }

    @Override
    public void onDataLoaded(List<Message> messages) {
        ChatContract.View<Model> view = getView();
        if (view == null) return;
        List<Message> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, messages);

    }

    @Override
    public void pushAudio(String path) {

    }

    @Override
    public void pushPics(List<String> pathList) {

    }

    @Override
    public boolean rePush(Message message) {
        if (message.getSender().getId().equalsIgnoreCase(Account.getUserId())
                && message.getStatus() == Message.STATUS_FAILED) {
            MessageCreateModel model = MessageCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }
        return false;
    }
}
