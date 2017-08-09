package com.douliu.italker.frags.assist;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.douliu.italker.R;
import com.douliu.italker.frags.media.GalleryFragment;
import com.example.commom.widget.PortraitView;
import com.example.commom.widget.recycler.RecyclerAdapter;
import com.example.factory.presenter.group.GroupCreateContract;

import butterknife.BindView;

/**
 *
 * Created by douliu on 2017/7/29.
 */

public class MemberFragment extends BottomSheetDialogFragment {

    private RecyclerView mRecycler;
    private RecyclerAdapter<GroupCreateContract.ViewModel> mAdapter;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_member, container, false);
        mRecycler = (RecyclerView) root.findViewById(R.id.recycler);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecycler();
    }

    private void setupRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler.setAdapter(mAdapter = new RecyclerAdapter<GroupCreateContract.ViewModel>() {
            @Override
            protected int getItemViewType(int position, GroupCreateContract.ViewModel model) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
                return new MemberFragment.ViewHolder(root);
            }
        });
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
            mPortraitView.setup(Glide.with(MemberFragment.this), model.author.getPortrait());
        }
    }

}
