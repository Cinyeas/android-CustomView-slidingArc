package com.custom.other;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


/**
 * Description：  光圈
 * Create by  ：  达选文
 * Create time：  2022-7-19
 * Contact    ：  13323392673（tell and wx）
 */
public class LightCircleView extends View {
    public static final String[] COLOR_ATTR_VALUE_ARR = new String[]{
            "#ffffff", "#2F0000", "#8E8E8E", "#CE0000", "#FF0080", "#E800E8", "#921AFF", "#2828FF",
            "#0072E3", "#00CACA", "#02DF82", "#00DB00", "#9AFF02", "#E1E100", "#EAC100", "#FF9224",
            "#FF5809", "#AD5A5A", "#A5A552", "#5CADAD", "#8080C0", "#AE57A4", "#E8E8D0", "#000079"};

    private Context mContext;
    private int mCircleSpace;
    private int mCircleDp = 12;          //荧光带
    private int mCenterX;
    private int mCenterY;
    private int mRadius;

    private int mPaddind = 0;
    private int maxMaskValue = 40;                                               //最大荧光值
    private int currentMaskValue = 1;                                              //当前荧光值
    private int mColorIndex = COLOR_ATTR_VALUE_ARR.length - 1;                    //当前颜色下标
    private String mPaintColor = COLOR_ATTR_VALUE_ARR[mColorIndex];               //当前颜色

    public LightCircleView(Context context) {
        this(context, null);
    }

    public LightCircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LightCircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public LightCircleView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mCircleSpace =dp2px(mContext, mCircleDp);

        int mViewWidth = getWidth();
        int mViewHeight = getHeight();
        mRadius = Math.min(mViewWidth, mViewHeight) / 2 - mCircleSpace / 4 * 3;

        mCenterX = mViewWidth / 2;
        mCenterY = mViewHeight / 2;

        if (getPaddingTop() != 0 && getPaddingBottom() != 0 && getPaddingLeft() != 0 && getPaddingRight() != 0) {
            mPaddind = getPaddingTop();
        }
    }

    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleSpace);
        mPaint.setColor(Color.parseColor(mPaintColor));
        if (currentMaskValue != 0) {
            mPaint.setMaskFilter(new BlurMaskFilter(currentMaskValue, BlurMaskFilter.Blur.SOLID));
        }
        canvas.drawCircle(mCenterX, mCenterY, (float) mRadius - mPaddind + 10, mPaint);
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    public void setPaintColor(int color) {
        this.mColorIndex = color;
        this.mPaintColor = getIndexPaintColor(color);
        invalidate();
    }

    /**
     * 设置画笔颜色
     *
     * @param color
     */
    public void setPaintColor(String color) {
        this.mPaintColor = color;
        invalidate();
    }

    /**
     * 返回指定颜色
     *
     * @param index
     * @return
     */
    public String getIndexPaintColor(int index) {
        return COLOR_ATTR_VALUE_ARR[index];
    }

    /**
     * 返回画笔颜色数
     *
     * @return
     */
    public int getPaintColorCount() {
        return COLOR_ATTR_VALUE_ARR.length;
    }

    /**
     * 返回当前画笔下标
     *
     * @return
     */
    public int getCurrentColorIndex() {
        return mColorIndex;
    }

    /**
     * 获取最大荧光值
     *
     * @return
     */
    public int getMaskValue() {
        return maxMaskValue;
    }

    /**
     * 获取当前荧光值
     *
     * @return
     */
    public int getCurrentMaskValue() {
        return currentMaskValue;
    }

    /**
     * 设置当前荧光值
     */
    public void setCurrentMaskValue(int index) {
        this.currentMaskValue = index;
        invalidate();
    }
}
