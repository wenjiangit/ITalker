package com.douliu.italker.frags.panel;


import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.douliu.italker.R;
import com.example.commom.app.BaseFragment;
import com.example.commom.face.Face;
import com.example.commom.utils.UiTool;

import net.qiujuer.genius.ui.Ui;

/**
 *
 */
public class PanelFragment extends BaseFragment {


    public PanelFragment() {
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_panel;
    }

    @Override
    protected void initWidget(View rootView) {
        super.initWidget(rootView);
        initFace(rootView);
        initRecord(rootView);
        initGallery(rootView);
    }

    private void initGallery(View rootView) {

    }

    private void initRecord(View rootView) {

    }

    private void initFace(View rootView) {
        View facePanel = rootView.findViewById(R.id.lay_face_panel);
        ViewPager pager = (ViewPager) facePanel.findViewById(R.id.pager);
        TabLayout tabLayout = (TabLayout) facePanel.findViewById(R.id.tab);
        tabLayout.setupWithViewPager(pager);
        pager.setAdapter(new MyPagerAdapter());

        ImageView backspace = (ImageView) rootView.findViewById(R.id.im_backspace);
        backspace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击删除
            }
        });

    }

    public void showFace() {

    }

    public void showRecord() {

    }

    public void showGallery() {

    }

    private class MyPagerAdapter extends PagerAdapter {

        private final int mSpanCount;

        MyPagerAdapter() {
            final int minFaceSize = (int) Ui.dipToPx(getResources(), 48);
            final int screenWidth = UiTool.getScreenWidth(getContext());
            mSpanCount = screenWidth / minFaceSize;
        }

        @Override
        public int getCount() {
            return Face.all(getContext()).size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            RecyclerView recyclerView = (RecyclerView) inflater
                    .inflate(R.layout.lay_face_content, container, false);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mSpanCount));

            return recyclerView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }



}
