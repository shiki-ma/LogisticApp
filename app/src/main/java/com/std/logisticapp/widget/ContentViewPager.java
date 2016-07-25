package com.std.logisticapp.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Maik on 2016/5/6.
 */
public class ContentViewPager extends ViewPager {
    public static boolean GO_TOUTH_CHILD = true;

    public ContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (GO_TOUTH_CHILD) {
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (GO_TOUTH_CHILD) {
            return false;
        }
        return super.onTouchEvent(ev);
    }
}
