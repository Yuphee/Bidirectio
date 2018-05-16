package com.zhangyf.bidirectio.ui.fragment;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;


import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.zhangyf.bidirectio.R;
import com.zhangyf.bidirectio.base.BaseFragment;
import com.zhangyf.bidirectio.base.GlideApp;
import com.zhangyf.bidirectio.ui.activity.PicDetailActivityVertical;

import javax.sql.DataSource;

import butterknife.BindView;

import static com.zhangyf.bidirectio.IntentConst.INTENT_URL;


/**
 * Created by zhangyf on 2018/1/30.
 */

public class PicFragment extends BaseFragment {

    public String mPicUrl;
    @BindView(R.id.pic)
    PhotoView pic;
    @BindView(R.id.progress)
    ProgressBar progress;

    @Override
    protected void onInitLazyData() {

    }

    @Override
    protected void onInitFastData() {
        mPicUrl = getArguments().getString(INTENT_URL);
    }

    @Override
    protected void onInitFastView() {

        pic.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                ((PicDetailActivityVertical) mActivity).onViewToggle();
            }
        });

        GlideApp.with(this)
                .load(mPicUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        if (progress != null) {
                            progress.setVisibility(View.GONE);
                        }
                        return false;
                    }

                })
                .into(pic);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_pic;
    }

}
