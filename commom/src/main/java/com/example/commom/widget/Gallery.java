package com.example.commom.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.commom.R;
import com.example.commom.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 画廊
 * Created by douliu on 2017/6/5.
 */

public class Gallery extends RecyclerView {

    private static final String TAG = "Galley";
    private static final int LOADER_ID = 0x0100;
    private static final int MAX_IMAGE_COUNT = 3;
    private static final int MIN_IMAGE_SIZE = 10 * 1024;
    private Adapter mAdapter;
    private LoaderCallback mLoaderCallback = new LoaderCallback();
    private List<Image> mSelectImages = new LinkedList<>();//适用于频繁地增加和删除操作
    private OnSelectChangeListener mListener;

    public Gallery(Context context) {
        super(context);
        init();
    }

    public Gallery(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Gallery(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));

        mAdapter = new Adapter();
        setAdapter(mAdapter);
        mAdapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Image>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder<Image> holder, Image image) {
                if (onItemSelected(image)) {
                    holder.updateData(image);
                }
            }
        });

    }

    public int setup(LoaderManager loaderManager, OnSelectChangeListener listener) {
        mListener = listener;
        loaderManager.initLoader(LOADER_ID, null, mLoaderCallback);
        return LOADER_ID;
    }

    /**
     * 图片点击操作
     *
     * @param image
     * @return 是否需要通知界面刷新
     */
    private boolean onItemSelected(Image image) {
        boolean notifyRefresh;
        if (mSelectImages.contains(image)) {
            mSelectImages.remove(image);
            image.isSelect = false;
            notifyRefresh = true;
        } else {
            if (mSelectImages.size() >= MAX_IMAGE_COUNT) {
                String str = getContext().getString(R.string.label_gallery_select_max_size);
                str = String.format(str, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(),str,Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                mSelectImages.add(image);
                image.isSelect = true;
                notifyRefresh = true;
            }
        }
        if (notifyRefresh) {
            notifySelectChanged();
        }

        return notifyRefresh;
    }

    private void notifySelectChanged() {
        if (mListener != null) {
            mListener.onSelectCountChange(mSelectImages.size());
        }
    }

    public void clear() {
        if (mSelectImages.size() > 0) {
            for (Image image : mSelectImages) {
                image.isSelect = false;
            }
            mSelectImages.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    public String[] getSelectedPath() {
        String[] paths = new String[mSelectImages.size()];
        int index = 0;
        for (Image image : mSelectImages) {
            paths[index++] = image.path;
        }
        return paths;
    }

    private static class Image {
        int id;
        String path;
        boolean isSelect;
        long date;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Image image = (Image) o;

            return path != null ? path.equals(image.path) : image.path == null;
        }

        @Override
        public int hashCode() {
            return path != null ? path.hashCode() : 0;
        }

        @Override
        public String toString() {
            return "Image{" +
                    "id=" + id +
                    ", path='" + path + '\'' +
                    ", isSelect=" + isSelect +
                    ", date=" + date +
                    '}';
        }
    }

    public interface OnSelectChangeListener {
        void onSelectCountChange(int count);
    }

    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {

        String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.ImageColumns._ID,//id
                MediaStore.Images.ImageColumns.DATA,//数据
                MediaStore.Images.ImageColumns.DATE_ADDED,//添加的日期
        };


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            if (id == LOADER_ID) {
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC" //按添加日期逆序排列
                );
            }
            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            if (cursor != null) {
                List<Image> images = new ArrayList<>();
                int count = cursor.getCount();
                if (count > 0) {
                    cursor.moveToFirst();
                    int idIndex = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int pathIndex = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int dateIndex = cursor.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = cursor.getInt(idIndex);
                        String path = cursor.getString(pathIndex);
                        long dateTime = cursor.getLong(dateIndex);

                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_SIZE) {
                            //如果图片不存在或太小则丢弃
                            continue;
                        }

                        Image image = new Image();
                        image.path = path;
                        image.date = dateTime;
                        image.id = id;
                        images.add(image);
                    } while (cursor.moveToNext());
                    //通知适配器刷新
                    updateSource(images);
                }
            }

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            updateSource(null);
        }
    }

    /**
     * 刷新数据源
     *
     * @param images
     */
    private void updateSource(List<Image> images) {
        Log.d(TAG, "updateSource: "+images);
        mAdapter.replace(images);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 适配器
     */
    private class Adapter extends RecyclerAdapter<Image> {

        @Override
        protected int getItemViewType(int position, Image image) {
            return R.layout.cell_galley;
        }

        @Override
        protected ViewHolder<Image> onCreateViewHolder(View root, int viewType) {
            return new Gallery.ViewHolder(root);
        }
    }


    /**
     * ViewHolder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<Image> {

        private final ImageView mImImage;
        private final View mShader;
        private final CheckBox mCbSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            mImImage = (ImageView) itemView.findViewById(R.id.im_image);
            mShader = itemView.findViewById(R.id.shader);
            mCbSelect = (CheckBox) itemView.findViewById(R.id.cb_select);
        }

        @Override
        public void onBind(Image image) {
            Glide.with(getContext())
                    .load(image.path)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.placeholderOf(R.color.grey_200))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))//不需要缓存
                    .into(mImImage);
            mShader.setVisibility(image.isSelect ? VISIBLE : INVISIBLE);
            mCbSelect.setChecked(image.isSelect);
            mCbSelect.setVisibility(VISIBLE);
        }
    }


}
