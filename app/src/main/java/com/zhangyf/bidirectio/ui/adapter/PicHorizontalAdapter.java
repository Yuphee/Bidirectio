package com.zhangyf.bidirectio.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;


import com.zhangyf.bidirectio.bean.PicBean;
import com.zhangyf.bidirectio.ui.fragment.PicFragment;
import com.zhangyf.vvp.EasyFragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.zhangyf.bidirectio.IntentConst.INTENT_URL;


/**
 * Created by zhangyf on 2017/9/8.
 */

public class PicHorizontalAdapter extends EasyFragmentStatePagerAdapter {

    private List<PicBean> photoSetInfo;

    public PicHorizontalAdapter(FragmentManager fm, List<PicBean> photoSetInfo) {
        super(fm);
        this.photoSetInfo = photoSetInfo;
    }

    @Override
    public Fragment getItem(int position) {
        if (photoSetInfo == null || photoSetInfo.size() == 0) {
            return null;
        }
        PicFragment fragment = new PicFragment();
        Bundle bundle = new Bundle();
        bundle.putString(INTENT_URL,photoSetInfo.get(position).getPic_url());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return photoSetInfo.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    public List<PicBean> getArticleBeanList() {
        return photoSetInfo;
    }

    public void setArticleBeanList(List<PicBean> moreArticleBeanList) {
        if (photoSetInfo == null) {
            photoSetInfo = new ArrayList<>();
        } else {
            photoSetInfo.clear();
        }
        photoSetInfo.addAll(moreArticleBeanList);
    }

}
