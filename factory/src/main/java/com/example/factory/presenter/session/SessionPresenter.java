package com.example.factory.presenter.session;

import com.example.commom.factory.presenter.BaseSourcePresenter;
import com.example.factory.data.message.SessionDataSource;
import com.example.factory.data.message.SessionRepository;
import com.example.factory.model.db.Session;

/**
 *
 * Created by wenjian on 2017/7/9.
 */

public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionDataSource, SessionContract.View>
        implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View view) {
        super(new SessionRepository(), view);
    }
}
