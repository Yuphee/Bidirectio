package com.zhangyf.bidirectio.ui.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;


import com.zhangyf.bidirectio.bean.ArticleBean;
import com.zhangyf.bidirectio.ui.fragment.PicHorizontalFragment;
import com.zhangyf.vvp.EasyFragmentStatePagerAdapter;

import java.util.List;

import static com.zhangyf.bidirectio.IntentConst.INTENT_ID;
import static com.zhangyf.bidirectio.IntentConst.INTENT_POS;


/**
 * Created by zhangyf on 2017/9/8.
 */

public class PicVerticalAdapter extends EasyFragmentStatePagerAdapter {

    private List<ArticleBean> mList;


    public PicVerticalAdapter(FragmentManager fm, List<ArticleBean> articleInfos) {
        super(fm);
        mList = articleInfos;
    }

    @Override
    public Fragment getItem(int position) {
        PicHorizontalFragment horizontalFragment = new PicHorizontalFragment();
        Bundle b = new Bundle();
        b.putInt(INTENT_ID,mList.get(position).getId());
        horizontalFragment.setArguments(b);
        return horizontalFragment;
    }

    @Override
    public int getCount() {
        if (mList == null || mList.size() == 0) return 0;
        return mList.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
