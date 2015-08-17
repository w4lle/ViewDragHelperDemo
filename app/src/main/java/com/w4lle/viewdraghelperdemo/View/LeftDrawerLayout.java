package com.w4lle.viewdraghelperdemo.View;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by w4lle on 15-8-17.
 * Copyright (c) 2015 Boohee, Inc. All rights reserved.
 */


/**
 * ViewDragHelper实现的左边侧滑菜单
 */
public class LeftDrawerLayout extends ViewGroup{
    public static final String TAG = LeftDrawerLayout.class.getSimpleName();
    private static final int MIN_DRAWER_MARGIN = 64; // dp
    /**
     * Minimum velocity that will be detected as a fling
     */
    private static final int MIN_FLING_VELOCITY = 400; // dips per second

    /**
     * drawer离父容器右边的最小外边距
     */
    private int mMinDrawerMargin;

    private View mLeftMenuView;
    private View mContentView;

    private ViewDragHelper mHelper;
    /**
     * drawer显示出来的占自身的百分比
     */
    private float mLeftMenuOnScrren;

    public LeftDrawerLayout(Context context) {
        this(context, null);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LeftDrawerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //setup drawer's minMargin
        final float density = getResources().getDisplayMetrics().density;
        final float minVel = MIN_FLING_VELOCITY * density;
        mMinDrawerMargin = (int) (MIN_DRAWER_MARGIN * density + 0.5f);

        mHelper = ViewDragHelper.create(this, 1.0f, new ViewDragHelper.Callback() {
            @Override
            public int clampViewPositionHorizontal(View child, int left, int dx) {
                Log.d(TAG, "clampViewPositionHorizontal");
                int newLeft = Math.max(-child.getWidth(), Math.min(left, 0));
                return newLeft;
            }

            @Override
            public int getViewHorizontalDragRange(View child) {
                Log.d(TAG, "getViewHorizontalDragRange");
                return mLeftMenuView == child ? child.getWidth() : 0;
            }

            //手指释放的时候回调
            @Override
            public void onViewReleased(View releasedChild, float xvel, float yvel) {
                Log.d(TAG, "onViewRealeased");
                final int childWidth = releasedChild.getWidth();
                float offset = (childWidth + releasedChild.getLeft()) * 1.0f / childWidth;
                mHelper.settleCapturedViewAt(xvel > 0 || xvel == 0 && offset > 0.5f ? 0 : -childWidth, releasedChild.getTop());
                invalidate();
            }

            @Override
            public void onViewCaptured(View capturedChild, int activePointerId) {
                Log.d(TAG, "onViewCaptured");
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
                Log.d(TAG, "onViewPositionChanged");
                final int childWidth = changedView.getWidth();
                float offset = (float) (childWidth + left) / childWidth;
                mLeftMenuOnScrren = offset;
                //offset can callback here
                changedView.setVisibility(offset == 0 ? View.INVISIBLE : View.VISIBLE);
                invalidate();
            }

            @Override
            public void onEdgeDragStarted(int edgeFlags, int pointerId) {
                Log.d(TAG, "onEdgeDragStarted");
                mHelper.captureChildView(mLeftMenuView, pointerId);
            }

            @Override
            public boolean tryCaptureView(View view, int i) {
                Log.d(TAG, "tryCaptureView");
                return view == mLeftMenuView;
            }
        });
        //设置edge_left track
        mHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
        //设置minVelocity
        mHelper.setMinVelocity(minVel);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(widthSize, heightSize);

        View leftMenuView = getChildAt(1);
        MarginLayoutParams lp = (MarginLayoutParams)
                leftMenuView.getLayoutParams();

        final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                mMinDrawerMargin + lp.leftMargin + lp.rightMargin,
                lp.width);
        final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                lp.topMargin + lp.bottomMargin,
                lp.height);
        leftMenuView.measure(drawerWidthSpec, drawerHeightSpec);


        View contentView = getChildAt(0);
        lp = (MarginLayoutParams) contentView.getLayoutParams();
        final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
        final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
        contentView.measure(contentWidthSpec, contentHeightSpec);

        mLeftMenuView = leftMenuView;
        mContentView = contentView;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        View menuView = mLeftMenuView;
        View contentView = mContentView;

        MarginLayoutParams lp = (MarginLayoutParams) contentView.getLayoutParams();
        contentView.layout(lp.leftMargin, lp.topMargin,
                lp.leftMargin + contentView.getMeasuredWidth(),
                lp.topMargin + contentView.getMeasuredHeight());

        lp = (MarginLayoutParams) menuView.getLayoutParams();

        final int menuWidth = menuView.getMeasuredWidth();
        int childLeft = -menuWidth + (int) (menuWidth * mLeftMenuOnScrren);
        menuView.layout(childLeft, lp.topMargin, childLeft + menuWidth,
                lp.topMargin + menuView.getMeasuredHeight());
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        if (mHelper.continueSettling(true)) {
            invalidate();
        }
    }

    public void closeDrawer()
    {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 0.f;
        mHelper.smoothSlideViewTo(menuView, -menuView.getWidth(), menuView.getTop());
    }

    public void openDrawer()
    {
        View menuView = mLeftMenuView;
        mLeftMenuOnScrren = 1.0f;
        mHelper.smoothSlideViewTo(menuView, 0, menuView.getTop());
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams()
    {
        return new MarginLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public LayoutParams generateLayoutParams(AttributeSet attrs)
    {
        return new MarginLayoutParams(getContext(), attrs);
    }

    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p)
    {
        return new MarginLayoutParams(p);
    }

}
