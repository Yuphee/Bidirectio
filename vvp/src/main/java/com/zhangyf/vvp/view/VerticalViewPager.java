package com.zhangyf.vvp.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.zhangyf.vvp.transformer.DefaultTransformer;


/**
 * Created by zhangyf on 2017/9/8.
 */

public class VerticalViewPager extends DelicacyViewPager {

    private int mLastX = 0;
    private int mLastY = 0;
    private boolean isSettling = false;
    private float lastPositionOffest;
    private int lastState;

    public VerticalViewPager(Context context) {
        this(context, null);
    }

    public VerticalViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPageTransformer(false, new DefaultTransformer());
        addOnPageChangeListener(new EasyViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
                    switch (state) {
                        case EasyViewPager.SCROLL_STATE_SETTLING:
                            isSettling = true;
                            lastState = EasyViewPager.SCROLL_STATE_SETTLING;
                            break;
                        case EasyViewPager.SCROLL_STATE_DRAGGING:
                            lastState = EasyViewPager.SCROLL_STATE_DRAGGING;
                            break;
                        case EasyViewPager.SCROLL_STATE_IDLE:
                            isSettling = false;
                            lastState = EasyViewPager.SCROLL_STATE_IDLE;
                            break;
                    }
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                LogUtils.e("zyfff","position:"+position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                lastPositionOffest = positionOffset;
//                LogUtils.e("zyfff","position:"+position+"positionOffset:"+positionOffset+"positionOffsetPixels:"+positionOffsetPixels);
            }
        });
    }

    private MotionEvent swapTouchEvent(MotionEvent event) {
        float width = getWidth();
        float height = getHeight();

        float swappedX = (event.getY() / height) * width;
        float swappedY = (event.getX() / width) * height;

        event.setLocation(swappedX, swappedY);

        return event;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
//                LogUtils.e("zyfff","lastState:"+lastState);
//                LogUtils.e("zyfff","lastPositionOffest:"+lastPositionOffest);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        super.onInterceptTouchEvent(swapTouchEvent(event));
        swapTouchEvent(event);
        boolean isIntercept = false;
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                int deltaX = x - mLastX;
                int deltaY = y - mLastY;
                if (Math.abs(deltaY) > Math.abs(deltaX) && event.getPointerCount() == 1) {
                    isIntercept = true;
                } else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
        }
        mLastX = x;
        mLastY = y;
        return isIntercept;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }



    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(swapTouchEvent(ev));
    }

    public boolean isSettling() {
        return isSettling;
    }

    public void setSettling(boolean settling) {
        isSettling = settling;
    }
}