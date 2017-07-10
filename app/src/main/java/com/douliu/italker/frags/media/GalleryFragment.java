package com.douliu.italker.frags.media;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.douliu.italker.R;
import com.example.commom.utils.UiTool;
import com.example.commom.widget.Gallery;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends BottomSheetDialogFragment implements Gallery.OnSelectChangeListener {

    private Gallery mGallery;

    private OnImageSelectedListener mListener;

    public GalleryFragment() {
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        mGallery = (Gallery) root.findViewById(R.id.gallery);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mGallery.setup(getLoaderManager(), this);
    }

    @Override
    public void onSelectCountChange(int count) {
        if (count > 0) {
            if (mListener != null) {
                String[] paths = mGallery.getSelectedPath();
                mListener.onImageSelect(paths[0]);
                //切断与外界的引用关系,有利于内存回收
                mListener = null;
            }
            dismiss();
        }
    }

    /**
     * 设置图片选择监听
     *
     * @param listener 监听器
     * @return 自己
     */
    public GalleryFragment setListener(OnImageSelectedListener listener) {
        mListener = listener;
        return this;
    }


    public interface OnImageSelectedListener {
        void onImageSelect(String path);
    }


    public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            final Window window = getWindow();
            if (window == null) {
                return;
            }

            int screenHeight = UiTool.getScreenHeigh(getOwnerActivity());
            int statusHeight = UiTool.getStatusHeight(getOwnerActivity());

            int dialogHeight = screenHeight - statusHeight;

            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT
                    , dialogHeight > 0 ? dialogHeight : ViewGroup.LayoutParams.MATCH_PARENT); }
    }

}
