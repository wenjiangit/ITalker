package com.douliu.italker.frags.account;


import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;

import com.douliu.italker.R;
import com.douliu.italker.activities.MainActivity;
import com.example.commom.app.PresenterFragment;
import com.example.factory.presenter.account.RegisterContract;
import com.example.factory.presenter.account.RegisterPresenter;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * RegisterFragment
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter>
        implements RegisterContract.View {

    @BindView(R.id.edit_phone)
    EditText mEditPhone;
    @BindView(R.id.edit_password)
    EditText mEditPassword;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.tv_login)
    TextView mTvLogin;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.loading)
    Loading mLoading;
    private AccountTrigger mTrigger;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof AccountTrigger) {
            mTrigger = (AccountTrigger) context;
        }

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }


    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String name = mEditName.getText().toString();
        String phone = mEditPhone.getText().toString();
        String password = mEditPassword.getText().toString();
        mPresenter.register(phone, name, password);
    }


    @OnClick(R.id.tv_login)
    void onGoLoginClick() {
        //AccountActivity进行界面切换
        mTrigger.triggerView();
    }

    @Override
    public void registerSuccess() {
        mLoading.stop();
        MainActivity.show(getContext());
        getActivity().finish();
    }

    @Override
    public void showError(int strId) {
        super.showError(strId);

        mLoading.stop();
        setWidgetEnable(true);
    }

    /**
     * 设置控件状态
     * @param enable 可否进行操作
     */
    private void setWidgetEnable(boolean enable) {
        mEditPassword.setEnabled(enable);
        mEditPhone.setEnabled(enable);
        mEditName.setEnabled(enable);
        mBtnSubmit.setEnabled(enable);
    }

    @Override
    public void showLoading() {
        super.showLoading();

        mLoading.start();
        setWidgetEnable(false);
    }

    @Override
    protected RegisterContract.Presenter createPresenter() {
        return new RegisterPresenter(this);
    }


}
