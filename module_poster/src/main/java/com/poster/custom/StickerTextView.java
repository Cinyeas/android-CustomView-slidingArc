package com.poster.custom;


import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.poster.utils.AutoResizeTextView;


/**
 * Created by cheungchingai on 6/15/15.
 */
public class StickerTextView extends StickerView {
    private AutoResizeTextView tv_main;
    private static int mColor;
    private static int mSize;
    private static String mText = "";

    public static void init(String text, int color, int size){
        mText = text;
        mColor = color;
        mSize = size;
    }

    public StickerTextView(Context context) {
        super(context);
    }

    public StickerTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public StickerTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public View getMainView() {
        if (tv_main != null)
            return tv_main;

        tv_main = new AutoResizeTextView(getContext());
        tv_main.setTextColor(mColor);
        tv_main.setGravity(Gravity.CENTER);
        tv_main.setTextSize(mSize);
        tv_main.setText(mText);
        //tv_main.setShadowLayer(4, 0, 0, Color.BLACK);
        tv_main.setMaxLines(1);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        tv_main.setLayoutParams(params);
        return tv_main;
    }

    public void setTextSize(int size) {
        this.mSize = size;
    }

    public int getTextSize(){
        return mSize;
    }

    public void setTextColor(int color) {
        this.mColor = color;
    }

    public int getTextColor() {
        return mColor;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public String getText() {
        if (tv_main != null)
            return tv_main.getText().toString();

        return null;
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    @Override
    protected void onScaling(boolean scaleUp) {
        super.onScaling(scaleUp);
    }
}
