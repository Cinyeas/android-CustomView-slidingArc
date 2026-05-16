package com.poster.utils;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * 设置控件显示的位置
 */
public class FloatingManager {
    private static FloatingManager manager;
    private View mAnchorView;
    private ViewGroup mParentView;
    private String mTitle;
    private int mWidth;
    private int mHeight;
    private int mX;
    private int mY;

    public static FloatingManager getInstance() {
        if (manager == null) {
            manager = new FloatingManager();
        }
        return manager;
    }

    public FloatingManager setAnchorView(View view) {
        this.mAnchorView = view;
        return this;
    }

    public FloatingManager setParentView(ViewGroup mParentView) {
        this.mParentView = mParentView;
        return this;
    }

    public FloatingManager setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public FloatingManager setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        return this;
    }

    public FloatingManager setXY(int x, int y) {
        this.mX = x;
        this.mY = y;
        return this;
    }

    public FloatingManager showCenterView() {
        if (mAnchorView == null) {
            return this;
        }
        Rect anchorRect = new Rect();
        Rect rootViewRect = new Rect();
        mAnchorView.getGlobalVisibleRect(anchorRect);
        mParentView.getGlobalVisibleRect(rootViewRect);

        if (mWidth != 0 && mHeight != 0) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mAnchorView.getLayoutParams();
            params.width = mWidth;
            params.height = mHeight;
            mAnchorView.setLayoutParams(params);                                       //设置居中显示
        }
        mAnchorView.setY(mY);
        mAnchorView.setX(mX);
        mParentView.addView(mAnchorView);                                            //调整显示区域大小
        return this;
    }
}