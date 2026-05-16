package com.custom.arc.menu;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import androidx.annotation.Nullable;

/**
 * Description：  组合View
 * Create by  ：  达选文
 * Create time：  2022-7-19
 * Contact    ：  13323392673（tell and wx）
 */
public class CombinationView extends androidx.appcompat.widget.AppCompatTextView {
    private Drawable mBackground = null;        //背景图
    private String mText = "";
    private int mTextColor = 0;

    private int textX = 0;
    private int textY = 0;
    private int mViewWidth = 0;

    public CombinationView(Context context) {
        this(context, null);
    }

    public CombinationView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CombinationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadViewInfo();
    }

    /**
     * 读取View信息
     */
    public void loadViewInfo() {
        mBackground = getBackground();
        setBackground(null);
        mText = getText().toString();
        setText("");
        mTextColor = getCurrentTextColor();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        /**
         * 取最长边的值，测量为正方形
         */
        int maxMeasureWidth = Math.max(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(maxMeasureWidth, maxMeasureWidth);
        mViewWidth = MeasureSpec.getSize(maxMeasureWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int mTextSize = textAutoSize((int) (mViewWidth / 2), mText);        //text的高度
        int mViewSpace = (int) Math.ceil(mTextSize * 0.6);

        int mImgViewHeight = mViewWidth - mTextSize - mViewSpace;
        int mImgLeft = (mViewWidth - mImgViewHeight) / 2;
        int mImgRight = mImgViewHeight + mImgLeft;

        textX = (mViewWidth - textWidth(mTextSize)) / 2;
        textY = mImgViewHeight + mTextSize / 2 + mViewSpace;

        Bitmap mBackBitmap = ((BitmapDrawable) mBackground).getBitmap();
        Rect mRect = new Rect(mImgLeft, 0, mImgRight, mImgViewHeight);
        canvas.drawBitmap(mBackBitmap, null, mRect, new Paint(Paint.ANTI_ALIAS_FLAG));

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTextColor);
        paint.setTextSize(mTextSize);
        canvas.drawText(mText, textX, textY, paint);
    }

    /**
     * 获取字体大小
     *
     * @return
     */
    private int textAutoSize(float screenWidth, String text) {
        // 计算 显示宽度(取控件宽度的 95%，不要取全部宽度，防止显示不全问题)
        float drawWidth = (screenWidth - getPaddingLeft() - getPaddingRight()) * 0.95f;
        float defTextWidth = getPaint().measureText(text);
        float proportion = drawWidth / defTextWidth;
        int textSize = (int) (getTextSize() * proportion);
        setTextSize(textSize);
        return textSize;
    }

    /**
     * 获取文本的宽度
     *
     * @param textSize
     * @return
     */
    private int textWidth(int textSize) {
        Paint tvPaint = new Paint();
        tvPaint.setTextSize(textSize);
        return (int) tvPaint.measureText(mText);
    }

    @Override
    public void setBackgroundResource(int resId) {
        super.setBackgroundResource(resId);
        mBackground = getBackground();
        setBackground(null);
    }

    public void setTextStr(String text) {
        this.mText = text;
    }

    public void setTvColor(int color) {
        this.mTextColor = color;
    }
}