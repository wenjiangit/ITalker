package com.douliu.italker.frags.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.douliu.italker.App;
import com.douliu.italker.R;
import com.douliu.italker.activities.MainActivity;
import com.douliu.italker.frags.media.GalleryFragment;
import com.example.commom.app.PresenterFragment;
import com.example.commom.widget.PortraitView;
import com.example.factory.presenter.user.UpdateInfoContract;
import com.example.factory.presenter.user.UpdateInfoPresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {

    private static final String TAG = "UpdateInfoFragment";

    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;
    @BindView(R.id.im_sex)
    ImageView mImSex;
    @BindView(R.id.edit_desc)
    EditText mEditDesc;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.loading)
    Loading mLoading;

    private String mPortraitFilePath;

    private boolean isMan = true;

    public UpdateInfoFragment() {
        // Required empty public constructor

    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @OnClick(R.id.im_portrait)
    void onPortraitClick() {
        new GalleryFragment()
                .setListener(new GalleryFragment.OnImageSelectedListener() {
                    @Override
                    public void onImageSelect(String path) {
                        File portraitTempFile = App.getPortraitTempFile();

                        Uri sourceUri = Uri.fromFile(new File(path));
                        Uri destinationUri = Uri.fromFile(portraitTempFile);

                        UCrop.Options options = new UCrop.Options();
                        //设置图片压缩格式
                        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                        //设置图片压缩精度
                        options.setCompressionQuality(96);

                        UCrop.of(sourceUri, destinationUri)
                                .withAspectRatio(1, 1)//设置比例
                                .withMaxResultSize(520, 520)//最大宽高
                                .withOptions(options)//相关配置
                                .start(getActivity());

                    }
                }).show(getChildFragmentManager(), GalleryFragment.class.getName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadPortrait(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            Log.e(TAG, "cropError: ", cropError);
        }
    }

    @OnClick(R.id.btn_submit)
    void onSubmit() {
        String desc = mEditDesc.getText().toString().trim();
        mPresenter.update(mPortraitFilePath, desc, isMan);
    }

    @OnClick(R.id.im_sex)
    void onSexClick() {
        isMan = !isMan;
        Drawable drawable = ContextCompat.getDrawable(getContext(), isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        mImSex.setImageDrawable(drawable);
        mImSex.getBackground().setLevel(isMan ? 0 : 1);
    }

    private void loadPortrait(Uri resultUri) {
        Glide.with(getActivity())
                .asBitmap()
                .load(resultUri)
                .apply(RequestOptions.centerCropTransform())
                .into(mImPortrait);

        mPortraitFilePath = resultUri.getPath();
    }

    @Override
    public void showError(int strId) {
        super.showError(strId);
        mLoading.stop();
        setWidgetEnable(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mLoading.start();
        setWidgetEnable(false);
    }

    @Override
    protected UpdateInfoContract.Presenter createPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @Override
    public void updateSucceed() {
        MainActivity.show(getContext());
        getActivity().finish();
    }

    /**
     * 设置控件状态
     * @param enable 可否进行操作
     */
    private void setWidgetEnable(boolean enable) {
        mImSex.setEnabled(enable);
        mEditDesc.setEnabled(enable);
        mImPortrait.setEnabled(enable);
        mBtnSubmit.setEnabled(enable);
    }

}
