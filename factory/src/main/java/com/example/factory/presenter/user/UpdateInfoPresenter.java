package com.example.factory.presenter.user;

import android.text.TextUtils;

import com.example.commom.factory.data.DataSource;
import com.example.commom.factory.presenter.BasePresenter;
import com.example.factory.Factory;
import com.example.factory.R;
import com.example.factory.data.helper.UserHelper;
import com.example.factory.model.api.UpdateInfoModel;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.User;
import com.example.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/**
 *
 *
 * Created by wenjian on 2017/6/18.
 */

public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter ,DataSource.Callback<UserCard>{

    public UpdateInfoPresenter(UpdateInfoContract.View view) {
        super(view);
    }

    @Override
    public void update(final String portraitFilePath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();
        //校验参数
        if (TextUtils.isEmpty(portraitFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_update_invalid_parameter);
            return;
        }

        Factory.runOnBackground(new Runnable() {
            @Override
            public void run() {
                //上传头像
                String url = UploadHelper.uploadPortrait(portraitFilePath);
                if (TextUtils.isEmpty(url)) {
                    Run.onUiAsync(new Action() {
                        @Override
                        public void call() {//强制切换到主线程
                            view.showError(R.string.data_rsp_error_unknown);
                        }
                    });
                } else {
                    UpdateInfoModel model = new UpdateInfoModel(url, desc,
                            isMan ? User.SEX_MAN : User.SEX_WOMAN);
                    UserHelper.update(model, UpdateInfoPresenter.this);
                }
            }
        });



    }

    @Override
    public void onDataLoaded(UserCard response) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSucceed();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null) {
            return;
        }
        //强制在主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.showError(strRes);
            }
        });
    }
}
