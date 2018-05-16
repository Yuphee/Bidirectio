package com.zhangyf.bidirectio.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhangyf.vvp.view.DelicacyViewPager;


/**
 * Created by zhangyf on 2017/9/8.
 */

public class PictureViewPager extends DelicacyViewPager {


    public PictureViewPager(Context context) {
        super(context);
    }

    public PictureViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

}
