package com.zhangyf.vvp.transformer;

import android.view.View;

import com.zhangyf.vvp.view.EasyViewPager;


/**
 * Created by zhangyf on 2017/9/8.
 */

public class DefaultTransformer implements EasyViewPager.PageTransformer {

    @Override
    public void transformPage(View view, float position) {
        float alpha = 0;
        if (0 <= position && position <= 1) {
            alpha = 1 - position;
        } else if (-1 < position && position < 0) {
            alpha = position + 1;
        }
        view.setAlpha(alpha);
        //Counteract the default slide transition
        view.setTranslationX(view.getWidth() * -position);
        float yPosition = position * view.getHeight();
        //set Y position to swipe in from top
        view.setTranslationY(yPosition);
    }
}
