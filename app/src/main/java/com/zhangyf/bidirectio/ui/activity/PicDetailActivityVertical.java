package com.zhangyf.bidirectio.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.zhangyf.bidirectio.MCache;
import com.zhangyf.bidirectio.R;
import com.zhangyf.bidirectio.base.BaseActivity;
import com.zhangyf.bidirectio.bean.ArticleBean;
import com.zhangyf.bidirectio.event.PicCachedEvent;
import com.zhangyf.bidirectio.event.PicFirstLoadedEvent;
import com.zhangyf.bidirectio.event.PosEvent;
import com.zhangyf.bidirectio.ui.adapter.PicVerticalAdapter;
import com.zhangyf.bidirectio.ui.fragment.PicHorizontalFragment;
import com.zhangyf.vvp.view.EasyViewPager;
import com.zhangyf.vvp.view.VerticalViewPager;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.zhangyf.bidirectio.IntentConst.INTENT_POS;


/**
 * Created by zhangyf on 2018/1/29.
 */

public class PicDetailActivityVertical extends BaseActivity {

    @BindView(R.id.vertical_pager)
    VerticalViewPager verticalPager;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_page)
    TextView tvPage;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    PicVerticalAdapter verticalAdapter;
    protected int verticalPos;
    protected int horizontalPos;
    protected int horizontalSize;

    public List<ArticleBean> articleInfos;
    public ArticleBean currentArticle;
    private boolean immersed;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_pic_detail;
    }

    @Override
    protected void initVariables(Bundle extras, Bundle savedInstanceState) {
        verticalPos = extras.getInt(INTENT_POS, 0);
        articleInfos = MCache.intentPic;
        currentArticle = articleInfos.get(verticalPos);
    }

    @Override
    protected void initViews() {
        verticalAdapter = new PicVerticalAdapter(getSupportFragmentManager(), articleInfos);
        verticalPager.setAdapter(verticalAdapter);
        verticalPager.setOffscreenPageLimit(2);
        verticalPager.addOnPageChangeListener(new EasyViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                verticalPos = position;
                currentArticle = articleInfos.get(verticalPos);
                if (verticalAdapter != null) {
                    PicHorizontalFragment horizontalFragment = (PicHorizontalFragment) verticalAdapter.instantiateItem(verticalPager, position);
                    if (horizontalFragment.photoInfo != null ) {
                        if (!horizontalFragment.isPost) {
                            horizontalFragment.loadData();
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        verticalPager.setCurrentItem(verticalPos);
    }

    @Override
    protected boolean shouldBindEvent() {
        return true;
    }

    @Subscribe
    public void onFirstLoadedPics(PicFirstLoadedEvent event) {
        horizontalPos = 0;
        horizontalSize = event.picSize;
        tvPage.setText(horizontalPos+1+"/"+horizontalSize);
    }

    @Subscribe
    public void onCachedPics(PicCachedEvent event) {
        horizontalPos = event.pos;
        tvPage.setText(horizontalPos+1+"/"+horizontalSize);
    }

    @Subscribe
    public void onHorizontalPosEvent(PosEvent event) {
        horizontalPos = event.pos;
        tvPage.setText(horizontalPos+1+"/"+horizontalSize);
    }

    @Override
    protected boolean isSteepStatusBar() {
        return super.isSteepStatusBar();
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    public void onViewToggle() {
        if (immersed) {
            rlTop.setVisibility(View.VISIBLE);
            llBottom.setVisibility(View.VISIBLE);
            immersed = false;
        } else {
            rlTop.setVisibility(View.INVISIBLE);
            llBottom.setVisibility(View.INVISIBLE);
            immersed = true;
        }
    }

    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
        }
    }
}
