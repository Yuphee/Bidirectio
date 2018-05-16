package com.zhangyf.bidirectio;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.zhangyf.bidirectio.base.BaseActivity;
import com.zhangyf.bidirectio.bean.ArticleBean;
import com.zhangyf.bidirectio.ui.activity.PicDetailActivityVertical;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhangyf.bidirectio.IntentConst.INTENT_POS;

public class MainActivity extends BaseActivity {

    @BindView(R.id.btn_go)
    Button btnGo;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initVariables(Bundle extras, Bundle savedInstanceState) {

    }

    @Override
    protected void initViews() {

    }

    @Override
    protected boolean shouldBindEvent() {
        return false;
    }

    @Override
    protected int getStatusBarColor() {
        return R.color.colorPrimary;
    }

    @OnClick(R.id.btn_go)
    public void onViewClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_go:
                List<ArticleBean> list = new ArrayList<>();
                for (int i = 0; i < 10; i++) {
                    ArticleBean articleBean = new ArticleBean();
                    articleBean.setId(i);
                    list.add(articleBean);
                }
                Bundle bundle = new Bundle();
                bundle.putInt(INTENT_POS,0);
                MCache.intentPic = list;
                readyGo(PicDetailActivityVertical.class,bundle);
                break;
        }
    }
}
