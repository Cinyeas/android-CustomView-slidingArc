package com.custom.other;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;

/**
 * Description：  电池电量
 * Create by  ：  达选文
 * Create time：  2022-7-19
 * Contact    ：  13323392673（tell and wx）
 */
public class BatteryView extends View {
    private int mWidth;    //主体外框总长
    private int mHeight;   //主体总高

    private int mMargin = 5;    //电池内芯与边框的距离
    private int mBoder = 4;     //电池外框的宽带
    private int mHeadWidth = 8; //头部宽度
    private int mHeadHeight = 10; //头部高度

    private RectF mMainRect;   //主体区域方位
    private RectF mHeadRect;  //头部区域方位

    private float mRadius = 4f;   //圆角
    private float mPower;
    private boolean mIsCharging;    //是否在充电

    public BatteryView(Context context) {
        this(context, null);
    }

    public BatteryView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mMainRect = new RectF(mBoder, mBoder, mWidth, mHeight - mBoder); //绘制主体区域大小
        float mLeft = mMainRect.width() + (mBoder * 2);
        float mTop = (mHeight - mHeadHeight) / 2;
        float mRight = mMainRect.width() + (mBoder) + mHeadWidth;
        float mBottom = (mHeight + mHeadHeight) / 2;
        mHeadRect = new RectF(mLeft, mTop, mRight, mBottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint paint1 = new Paint();
        //画外框
        paint1.setStyle(Paint.Style.STROKE);    //设置空心矩形
        paint1.setStrokeWidth(mBoder);          //设置边框宽度
        paint1.setColor(Color.BLACK);
        canvas.drawRoundRect(mMainRect, mRadius, mRadius, paint1);

        //画电池头
        paint1.setStyle(Paint.Style.FILL);  //实心
        paint1.setColor(Color.BLACK);
        canvas.drawRect(mHeadRect, paint1);

        //画电池芯
        Paint paint = new Paint();
        if (mIsCharging) {
            paint.setColor(Color.GREEN);
        } else {
            if (mPower < 0.1) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.BLACK);
            }
        }

        int width = (int) (mPower * (mMainRect.width() - mMargin * 2));
        int left = (int) (mMainRect.left + mMargin);
        int right = (int) (mMainRect.left + mMargin + width);
        int top = (int) (mMainRect.top + mMargin);
        int bottom = (int) (mMainRect.bottom - mMargin);
        Rect rect = new Rect(left, top, right, bottom);
        canvas.drawRect(rect, paint);
    }

/*    @Override  //设置 view 大小
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //setMeasuredDimension(mWidth, mHeight);
    }*/

    public void setPower(float power) {
        mPower = power;
        invalidate();
    }
}