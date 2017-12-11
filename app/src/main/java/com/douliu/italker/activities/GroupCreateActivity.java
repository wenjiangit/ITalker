package com.douliu.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.douliu.italker.App;
import com.douliu.italker.R;
import com.douliu.italker.frags.media.GalleryFragment;
import com.example.commom.app.PresenterToolbarActivity;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.presenter.group.GroupCreateContract;
import com.example.factory.presenter.group.GroupCreatePresenter;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.EditText;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author wenjian
 */
public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter>
        implements GroupCreateContract.View {

    private static final String TAG = "GroupCreateActivity";

    @BindView(R.id.im_portrait)
    PortraitView mImPortrait;
    @BindView(R.id.edit_name)
    EditText mEditName;
    @BindView(R.id.edit_desc)
    EditText mEditDesc;
    @BindView(R.id.recycler)
    RecyclerView mRecycler;

    private RecyclerAdapter<GroupCreateContract.ViewModel> mAdapter;
    private String mPortraitFilePath;

    public static void show(Context context) {
        App.startActivity(context, GroupCreateActivity.class);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        setTitle("");
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.setHasFixedSize(true);
        mAdapter = new Adapter();
        mRecycler.setAdapter(mAdapter);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<GroupCreateContract.ViewModel>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel> holder,
                                    GroupCreateContract.ViewModel model) {
                boolean selected = model.isSelected;
                model.isSelected = !selected;
                holder.updateData(model);
                mPresenter.changeSelected(model, model.isSelected);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            onCreateClick();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
                                .start(GroupCreateActivity.this);

                    }
                }).show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    private void onCreateClick() {
        String name = mEditName.getText().toString().trim();
        String desc = mEditDesc.getText().toString().trim();
        mPresenter.create(name, desc, mPortraitFilePath);
        hideSoftKeyBoard();
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

    private void loadPortrait(Uri resultUri) {
        Glide.with(this)
                .asBitmap()
                .load(resultUri)
                .apply(RequestOptions.centerCropTransform())
                .into(mImPortrait);

        mPortraitFilePath = resultUri.getPath();
    }


    @Override
    public void onCreateSucceed() {
        hideLoading();
        App.showToast(R.string.label_group_create_succeed);
        finish();
    }

    /**
     * 隐藏软键盘
     */
    private void hideSoftKeyBoard() {
        View focus = getCurrentFocus();
        if (focus == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }

    @Override
    protected GroupCreateContract.Presenter createPresenter() {
        return new GroupCreatePresenter(this);
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return mAdapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel>{

        @Override
        protected int getItemViewType(int position, GroupCreateContract.ViewModel model) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
        }
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel>{

        @BindView(R.id.txt_name)
        TextView mTxtName;
        @BindView(R.id.cb_select)
        CheckBox mCbSelect;
        @BindView(R.id.im_portrait)
        PortraitView mPortraitView;

        ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onBind(GroupCreateContract.ViewModel model) {
            mTxtName.setText(model.author.getName());
            mCbSelect.setChecked(model.isSelected);
            mPortraitView.setup(Glide.with(GroupCreateActivity.this), model.author.getPortrait());
        }
    }

}
