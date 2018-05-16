package com.zhangyf.bidirectio.ui.fragment;

import com.zhangyf.bidirectio.R;
import com.zhangyf.bidirectio.base.BaseFragment;
import com.zhangyf.bidirectio.bean.PicBean;
import com.zhangyf.bidirectio.event.PicCachedEvent;
import com.zhangyf.bidirectio.event.PicFirstLoadedEvent;
import com.zhangyf.bidirectio.event.PosEvent;
import com.zhangyf.bidirectio.ui.activity.PicDetailActivityVertical;
import com.zhangyf.bidirectio.ui.adapter.PicHorizontalAdapter;
import com.zhangyf.bidirectio.views.PictureViewPager;
import com.zhangyf.vvp.view.EasyViewPager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.zhangyf.bidirectio.IntentConst.INTENT_ID;

/**
 * Created by zhangyf on 2018/1/30.
 */

public class PicHorizontalFragment extends BaseFragment {

    public List<PicBean> photoInfo;
    public PicHorizontalAdapter horizontalAdapter;
    @BindView(R.id.vp_vpic)
    PictureViewPager vpVpic;
    private int articleId;
    public boolean isInit;
    public boolean isPost;
    public boolean isPosted;
    private int currentPos;

    @Override
    protected void onInitLazyData() {

    }

    @Override
    protected void onInitFastData() {
        articleId = getArguments().getInt(INTENT_ID,-1);
        photoInfo = new ArrayList<>();
    }

    @Override
    protected void onInitFastView() {
        loadData();
    }

    public void loadData() {
        // 去网络请求
        if(photoInfo.size() <= 0) {
            List<PicBean> list = new ArrayList<>();
            for (int i = 0; i < 25; i++) {
                PicBean pic = new PicBean();
                pic.setPic_url("http://onz34txkn.bkt.clouddn.com/mm.jpg");
                list.add(pic);
            }
            photoInfo.addAll(list);
        }

        if (((PicDetailActivityVertical) mActivity).currentArticle.getId() == articleId) {
            if(!isPosted) {
                EventBus.getDefault().post(new PicFirstLoadedEvent(photoInfo.size(),articleId));
                isPosted = true;
            }else {
                EventBus.getDefault().post(new PicCachedEvent(photoInfo.size(),articleId,currentPos));
            }
        }
        if (!isInit) {
            isInit = true;
            horizontalAdapter = new PicHorizontalAdapter(getChildFragmentManager(), photoInfo);
            vpVpic.setOffscreenPageLimit(2);
            vpVpic.setAdapter(horizontalAdapter);
            vpVpic.addOnPageChangeListener(new EasyViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentPos = position;
                    EventBus.getDefault().post(new PosEvent(position));
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });

        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_v_pic;
    }

}
