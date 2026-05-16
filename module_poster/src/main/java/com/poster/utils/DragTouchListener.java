package com.poster.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IntDef;
import com.poster.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 拖拽操作
 */
public class DragTouchListener implements View.OnTouchListener {

    @IntDef({HORIZONTAL, VERTICAL, FREE})
    @Retention(RetentionPolicy.SOURCE)
    private @interface OrientationMode {
    }

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int FREE = 2;

    private float curX;
    private float curY;
    private float startX;
    private float startY;
    private float tranX;
    private float tranY;
    private int direct = FREE;

    private final View view;
    private OnDragListener listener;
    private int maxDistance = -1;
    private boolean isFinish = false;

    public DragTouchListener(View view) {
        this.view = view;
    }

    DragTouchListener(View view, @OrientationMode int direct) {
        this.view = view;
        this.direct = direct;
    }

    public void setDirect(@OrientationMode int direct) {
        this.direct = direct;
    }

    public void setAllowedMaxDistance(int distance) {
        if (distance >= -1) {
            this.maxDistance = distance;
        }
    }

    private boolean isInAllowDistance() {
        if (maxDistance == -1) {
            return true;
        }
        if (direct == HORIZONTAL) {
            return maxDistance > Math.abs(curX - startX);
        } else if (direct == VERTICAL) {
            return maxDistance > Math.abs(curY - startY);
        } else {
            float dx = curX - startX;
            float dy = curY - startY;
            return maxDistance > Math.sqrt(dx * dx + dy * dy);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                tranX = view.getTranslationX();
                tranY = view.getTranslationY();
                isFinish = false;
                setBorder(view);
                break;

            case MotionEvent.ACTION_MOVE:
                if (isFinish) {
                    break;
                }
                curX = event.getRawX();
                curY = event.getRawY();
                if (direct == FREE) {
                    view.setTranslationX(tranX + curX - startX);
                    view.setTranslationY(tranY + curY - startY);
                } else if (direct == HORIZONTAL) {
                    view.setTranslationX(tranX + curX - startX);
                } else if (direct == VERTICAL) {
                    view.setTranslationY(tranY + curY - startY);
                }
                if (listener != null) {
                    listener.onDragging(view);
                }
                if (!isInAllowDistance() && listener != null) {
                    listener.onDragFinish(view);
                    isFinish = true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (listener != null && !isFinish) {
                    listener.onDragFinish(view);
                    isFinish = true;
                }
                break;
        }
        return true;
    }

    /**
     * 设置边框
     *
     * @param view
     */
    private void setBorder(View view) {
        ViewGroup mParent = (ViewGroup) view.getParent();
        for (int index = 0; index < mParent.getChildCount(); index++) {
            View child = mParent.getChildAt(index);
            child.setBackground(null);
        }
        view.setBackgroundResource(R.drawable.border_view);
    }

    public void reset() {
        view.setTranslationX(0);
        view.setTranslationY(0);
    }

    public void setOnDragListener(OnDragListener listener) {
        this.listener = listener;
    }

    public interface OnDragListener {
        void onDragging(View view);

        void onDragFinish(View view);
    }
}