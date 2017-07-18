package com.example.factory.presenter.session;

import android.support.v7.util.DiffUtil;

import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.data.message.SessionDataSource;
import com.example.factory.data.message.SessionRepository;
import com.example.factory.model.db.Session;
import com.example.factory.utils.DiffUiDataCallback;

import java.util.List;

/**
 *
 * Created by wenjian on 2017/7/9.
 */

public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }

    @Override
    public void onDataLoaded(List<Session> response) {
        SessionContract.View view = getView();
        if (view == null) {
            return;
        }

        RecyclerAdapter<Session> adapter = view.getRecyclerAdapter();
        List<Session> items = adapter.getItems();

        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(items, response);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);

        refreshData(diffResult,response);
    }
}
