package com.oddsix.nutripro.compontents;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by mobile2you on 30/09/16.
 */

public class UnswipableViewPager extends ViewPager {
    public UnswipableViewPager(Context context) {
        super(context);
    }

    public UnswipableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }
}
