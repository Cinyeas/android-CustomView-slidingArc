package com.custom.arc.menu;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import androidx.annotation.Nullable;
import com.arc.menu.R;

import java.util.ArrayList;

/**
 * Description：  弧形菜单
 * Create by  ：  达选文
 * Create time：  2022-7-19
 * Contact    ：  13323392673（tell and wx）
 */
public class SemiCircleMenuView extends ViewGroup {
    private Context mContext;
    private MenuInterface menuIml;
    private CanvasCallBackInterface mCanvasIml;

    public static int NORMAL_IMG_VIEW_TAG = 0;
    public static int SHARED_IMG_VIEW_TAG = 100;
    public static int VOICE_IMG_VIEW_TAG = 101;
    public static int SET_IMG_VIEW_TAG = 102;
    public static int USER_IMG_VIEW_TAG = 103;
    public static int RESET_IMG_VIEW_TAG = 104;

    private int mBuAreaTag = 0;                        //记录点击区域标识
    private int mLastAreaTag = 0;                      //记录上一个点击区域标识

    /**
     * ViewGroup的宽度高度
     */
    private float mViewWidth;
    private float mViewHeight;

    /**
     * 大小圆半径
     */
    private double mBigCircleRadius;
    private int mArcCentre;                     //圆中心点到两圆弧中心点的距离
    private int mSmallCircleRadius;

    /**
     * 圆心坐标
     */
    private float mChildrenCenterX;
    private float mChildrenCenterY;

    /**
     * 两个圆之间的间距
     * 单位：dp
     */
    private int mCircleDp = 90;
    private int mCircleSpace;
    private int mChildPadding;

    /**
     * 底部弧形菜单坐标
     * 底部弧形菜单背景起始角度
     *
     * @param context
     */
    private ArrayList<ArrayList<Integer>> mViewCoordArr;
    private ArrayList<Integer> mBgAngleArr;

    /**
     * 底部取消按钮的坐标
     *
     * @param context
     */
    private int mCancleImgTop = 0;
    private int mCancleImgBottom = 0;
    private int mCancleImgLeft = 0;
    private int mCancleImgRight = 0;

    /**
     * 角度值
     */
    private boolean mDrawAnimationStatus = false;
    private float mCurData = 0;
    private int startAngle = 0;
    private int sweepAngle = 0;
    private int mBuffStrartAngle = 0;
    private int mBuffSweepAngle = 0;

    /**
     * 整个控件的背景颜色
     */
    private int mWidgetBgColor = getResources().getColor(R.color.white);

    /**
     * 尺寸控制
     */
    private int mSizeOffset = 2;

    public SemiCircleMenuView(Context context) {
        this(context, null);
    }

    public SemiCircleMenuView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SemiCircleMenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SemiCircleMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.mContext = context;
    }

    /**
     * 将布局参数应用到子View，驱动子View进行自身测量
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        params(widthMeasureSpec, heightMeasureSpec);

        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.VISIBLE) {
                ArrayList<Integer> zuobiao = mViewCoordArr.get(index);
                int mLeft = zuobiao.get(0) + mSizeOffset;
                int mRight = zuobiao.get(1) - mSizeOffset;
                int mTop = zuobiao.get(2) + mSizeOffset;
                int mBottom = zuobiao.get(3) - mSizeOffset;
                measureChild(childView, mRight - mLeft, mBottom - mTop);
                index = index + 1;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int index = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == View.VISIBLE) {
                ArrayList<Integer> zuobiao = mViewCoordArr.get(index);
                int mLeft = zuobiao.get(0) + mSizeOffset;
                int mRight = zuobiao.get(1) - mSizeOffset;
                int mTop = zuobiao.get(2) + mSizeOffset;
                int mBottom = zuobiao.get(3) - mSizeOffset;

                childView.layout(mLeft, mTop, mRight, mBottom);
                index = index + 1;
            }
        }
    }

    /**
     * 计算参数信息
     */
    private void params(int widthMeasureSpec, int heightMeasureSpec) {
        mViewCoordArr = new ArrayList<>();
        mBgAngleArr = new ArrayList<>();

        int showSize = showViewSize();
        mCircleSpace = dp2px(mContext, mCircleDp);
        mChildPadding = mCircleSpace / 4;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);

        mSmallCircleRadius = Math.round((float) (mViewWidth / 2 / Math.cos(Math.PI / 2 - Math.PI / 5)));
        mArcCentre = mSmallCircleRadius + mCircleSpace / 2;            //圆中心点到两圆弧中心点的距离
        mBigCircleRadius = mSmallCircleRadius + mCircleSpace;

        mChildrenCenterX = mViewWidth / 2;
        mChildrenCenterY = (float) mBigCircleRadius;

        /**
         * Math.toDegrees  弧度转角度
         * mMenuAngle：扇形菜单的角度
         */
        int mMenuAngle = (int) Math.toDegrees(Math.acos((mBigCircleRadius * mBigCircleRadius * 2 - mViewWidth * mViewWidth) / (2.0 * mBigCircleRadius * mBigCircleRadius)));
        int mStartAngle = 90 - mMenuAngle / 2;                                            //起始角度
        float mAverageAngle = (float) mMenuAngle / (showSize * 2);          //每个角度度数及其中心点的角度值
        int mCoordAngle = (int) (mStartAngle + mAverageAngle);                            //坐标起始角度
        mBgAngleArr.add(mStartAngle);

        for (int i = 0; i < showSize; i++) {
            mStartAngle = (int) (mStartAngle + 2 * mAverageAngle);
            mBgAngleArr.add(mStartAngle);
            mViewCoordArr.add(menuImgCoord(mCoordAngle));
            mCoordAngle = (int) (mCoordAngle + 2 * mAverageAngle);
        }
    }

    /**
     * 获取显示的View个数
     *
     * @return
     */
    private int showViewSize() {
        int count = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView.getVisibility() == VISIBLE) {
                count = count + 1;
            }
        }
        return count;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawArcLine(canvas);
        drawLogoImg(canvas);
        drawArcBg(canvas);
        super.dispatchDraw(canvas);
        if (mCanvasIml != null) {
            mCanvasIml.deawFinsh();
        }
    }

    /**
     * 绘制菜单底部logo
     * mSizeRatio :计算原图片的宽高比
     *
     * @param canvas
     */
    private void drawLogoImg(Canvas canvas) {
        Bitmap bitmap = null;
        if (mBuAreaTag == NORMAL_IMG_VIEW_TAG || mBuAreaTag == RESET_IMG_VIEW_TAG || mBuAreaTag == USER_IMG_VIEW_TAG) {
            bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.main_logo)).getBitmap();
            float mSizeRatio = (float) ((int) (((float) bitmap.getWidth() / bitmap.getHeight()) * 100)) / 100;
            mCancleImgLeft = (int) (mViewWidth / 10 * 4);
            mCancleImgRight = (int) (mViewWidth / 10 * 6);

            int mSpaceY = (int) ((mViewHeight - mCircleSpace) / 5);
            mCancleImgTop = mCircleSpace + 2 * mSpaceY;
            mCancleImgBottom = mCancleImgTop + (int) ((mCancleImgRight - mCancleImgLeft) / mSizeRatio);
        } else {
            int mCenter = (int) ((mViewHeight - mCircleSpace) / 2);
            mCancleImgTop = mCircleSpace + mCenter - 40;
            mCancleImgBottom = mCircleSpace + mCenter + 40;
            mCancleImgLeft = (int) (mViewWidth / 2 - 40);
            mCancleImgRight = (int) (mViewWidth / 2 + 40);
            bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.main_x)).getBitmap();
        }
        Rect mRect = new Rect(mCancleImgLeft, mCancleImgTop, mCancleImgRight, mCancleImgBottom);
        canvas.drawBitmap(bitmap, null, mRect, new Paint(Paint.ANTI_ALIAS_FLAG));
    }

    /**
     * 绘制底部菜单背景
     *
     * @param canvas
     */
    private void drawArcBg(Canvas canvas) {
        LinearGradient lg = new LinearGradient(0, 0, getMeasuredWidth(), 0,
                new int[]{Color.parseColor("#1CE63A"), Color.parseColor("#E8DB5C")}, new float[]{0, 1.0f}, Shader.TileMode.CLAMP);

        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mCircleSpace);
        mPaint.setColor(Color.parseColor("#1CE63A"));
        mPaint.setShader(lg);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        float left = (float) (mChildrenCenterX - mSmallCircleRadius - mCircleSpace / 2);
        float top = (float) (mChildrenCenterY - mSmallCircleRadius - mCircleSpace / 2);
        float right = (float) (mChildrenCenterX + mSmallCircleRadius + mCircleSpace / 2);
        float bottom = (float) (mChildrenCenterY + mSmallCircleRadius + mCircleSpace / 2);

        RectF mRectF = new RectF(left, top, right, bottom);
        drawForClickType(canvas, mRectF, mPaint);
    }

    /**
     * 对View进行判断
     */
    private void drawForClickType(Canvas canvas, RectF mRectF, Paint mPaint) {
        if (mBuAreaTag == NORMAL_IMG_VIEW_TAG) {
            startAngle = 180 + mBgAngleArr.get(0) + 5;
            sweepAngle = mBgAngleArr.get(3) - mBgAngleArr.get(0) - 12;
            mBuffStrartAngle = startAngle;
            mBuffSweepAngle = sweepAngle;
        } else if (mBuAreaTag == RESET_IMG_VIEW_TAG) {
            int mAnimationAngle = 0;
            if (mLastAreaTag == USER_IMG_VIEW_TAG) {
                startAngle = (int) (mBuffStrartAngle + mBuffSweepAngle + (mBgAngleArr.get(4) - mBgAngleArr.get(3)) - mCurData);
                sweepAngle = -mBuffSweepAngle;
                mAnimationAngle = mBgAngleArr.get(4) - mBgAngleArr.get(3);
            } else {
                sweepAngle = mBuffSweepAngle;
                startAngle = (int) (mBuffStrartAngle - mBuffSweepAngle + (mBgAngleArr.get(1) - mBgAngleArr.get(0)) + mCurData);
                mAnimationAngle = sweepAngle - (mBgAngleArr.get(1) - mBgAngleArr.get(0));
            }
            if (!mDrawAnimationStatus) {
                mDrawAnimationStatus = true;
                setPercentData(mAnimationAngle);
            }
        } else if (mBuAreaTag == VOICE_IMG_VIEW_TAG) {
            sweepAngle = -mBuffSweepAngle + 4;
            startAngle = (int) (mBuffStrartAngle + mBuffSweepAngle - mCurData);
            if (!mDrawAnimationStatus) {
                mDrawAnimationStatus = true;
                setPercentData(sweepAngle);
            }
        } else if (mBuAreaTag == SET_IMG_VIEW_TAG) {
            sweepAngle = -mBuffSweepAngle + 3;
            startAngle = (int) (mBuffStrartAngle + mBuffSweepAngle - mCurData);
            if (!mDrawAnimationStatus) {
                mDrawAnimationStatus = true;
                setPercentData(sweepAngle);
            }
        } else if (mBuAreaTag == USER_IMG_VIEW_TAG) {
            startAngle = (int) (mBuffStrartAngle + mCurData) + 1;
            sweepAngle = mBuffSweepAngle + 2;
            if (!mDrawAnimationStatus) {
                mDrawAnimationStatus = true;
                setPercentData(mBgAngleArr.get(4) - mBgAngleArr.get(3));
            }
        }
        canvas.drawArc(mRectF, startAngle, sweepAngle, false, mPaint);
    }

    public void setPercentData(float endAngle) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(mCurData, Math.abs(endAngle));
        valueAnimator.setDuration((long) (Math.abs(endAngle) * 1000 / 80));
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            mCurData = (float) valueAnimator1.getAnimatedValue();
            invalidate();
        });
        valueAnimator.setInterpolator(new AccelerateInterpolator());
        valueAnimator.start();
    }

    /**
     * 给Paint设置setStrokeCap时
     * 左右会增加一个StrokeWidth的宽度
     * 在这儿根据余弦定理求StrokeWidth的角度
     *
     * @return
     */
    private int paintStrokeCapAngle() {
        double mAcs = Math.acos((float) (2 * mArcCentre * mArcCentre - mCircleSpace * mCircleSpace) / (2 * mArcCentre * mArcCentre));
        return (int) Math.toDegrees(mAcs);
    }

    /**
     * 底部菜单的两条圆弧线
     *
     * @param canvas
     */
    private void drawArcLine(Canvas canvas) {
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);

        mPaint.setColor(getResources().getColor(R.color.bgblack));
        canvas.drawCircle(mChildrenCenterX, mChildrenCenterY, (float) mBigCircleRadius, mPaint);

        mPaint.setColor(mWidgetBgColor);
        canvas.drawCircle(mChildrenCenterX, mChildrenCenterY, (float) mSmallCircleRadius, mPaint);
    }

    /**
     * 四个图标的坐标点
     *
     * @param mStartAngle 图标开始角度
     * @return
     */
    private ArrayList<Integer> menuImgCoord(int mStartAngle) {
        ArrayList<Integer> mList = new ArrayList();

        /**
         * Math.toRadians    以度为单位的角度转换为用弧度表示的近似相等的角度
         * 正弦，在直角三角形中，任意一锐角∠A的对边与斜边的比叫做∠A的正弦，记作sinA
         *
         * lineA：起始角度的对边
         * lineB：圆心到lineA的距离
         */
        int lineA = (int) (mArcCentre * Math.sin(Math.toRadians(mStartAngle)));
        int lineB = (int) Math.sqrt(mArcCentre * mArcCentre - lineA * lineA);

        if (mStartAngle > 90) {
            mList.add((int) (mViewWidth / 2 + lineB - mChildPadding));          //左
            mList.add((int) (mViewWidth / 2 + lineB + mChildPadding));          //右
        } else {
            mList.add((int) (mViewWidth / 2 - lineB - mChildPadding));          //左
            mList.add((int) (mViewWidth / 2 - lineB + mChildPadding));          //右
        }
        mList.add((int) (mChildrenCenterY - lineA - mChildPadding));            //顶
        mList.add((int) (mChildrenCenterY - lineA + mChildPadding));            //底
        return mList;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            ArrayList<Integer> mShardImg = mViewCoordArr.get(0);
            ArrayList<Integer> mVoiceImg = mViewCoordArr.get(1);
            ArrayList<Integer> mSetImg = mViewCoordArr.get(2);

            ArrayList<Integer> mUserImg = null;
            if (mViewCoordArr.size() == 4) {
                mUserImg = mViewCoordArr.get(3);
            }

            float mTouchx = event.getX();
            float mTouchy = event.getY();
            if (mTouchx >= mShardImg.get(0) && mTouchx <= mShardImg.get(1)
                    && mTouchy >= mShardImg.get(2) && mTouchy <= mShardImg.get(3)) {
                menuIml.onClickShared(mBuAreaTag);
            } else if (mTouchx >= mVoiceImg.get(0) && mTouchx <= mVoiceImg.get(1)
                    && mTouchy >= mVoiceImg.get(2) && mTouchy <= mVoiceImg.get(3)) {
                menuIml.onClickVoice(mBuAreaTag);
            } else if (mTouchx >= mSetImg.get(0) && mTouchx <= mSetImg.get(1)
                    && mTouchy >= mSetImg.get(2) && mTouchy <= mSetImg.get(3)) {
                menuIml.onClickSet(mBuAreaTag);
            } else {
                if (mUserImg != null) {
                    if (mTouchx >= mUserImg.get(0) && mTouchx <= mUserImg.get(1)
                            && mTouchy >= mUserImg.get(2) && mTouchy <= mUserImg.get(3)) {
                        menuIml.onClickUser(mBuAreaTag);
                    }
                }
            }

            /**
             * 底部小叉叉的点击事件
             */
            if (mTouchx >= mCancleImgLeft && mTouchx <= mCancleImgRight
                    && mTouchy >= mCancleImgTop && mTouchy <= mCancleImgBottom) {
                if (mBuAreaTag == VOICE_IMG_VIEW_TAG || mBuAreaTag == SET_IMG_VIEW_TAG) {
                    menuIml.onReLoadMenu();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    /**
     * 设置动画的绘制状态
     */
    public void resetAnimationStatus() {
        this.mDrawAnimationStatus = false;
        this.mCurData = 0;
    }

    /**
     * 设置当前选择的按钮TAG
     *
     * @return
     */
    public void setImgBuTag(int tag) {
        this.mBuAreaTag = tag;
    }

    /**
     * 设置控件背景颜色
     */
    public void setWidgetBgColor(int color) {
        this.mWidgetBgColor = color;
    }

    /**
     * 设置上一个选择的按钮TAG
     *
     * @return
     */
    public void setLastAreaTag(int tag) {
        this.mLastAreaTag = tag;
    }

    public void setMenuIml(MenuInterface iml) {
        this.menuIml = iml;
    }

    public void setCanvasIml(CanvasCallBackInterface canvasIml) {
        this.mCanvasIml = canvasIml;
    }

    /**
     * 区域点击事件回调
     */
    public interface MenuInterface {
        void onClickShared(int flag);

        void onClickVoice(int flag);

        void onClickSet(int flag);

        void onClickUser(int flag);

        void onReLoadMenu();
    }

    /**
     * 绘制完成后回调
     */
    public interface CanvasCallBackInterface {
        void deawFinsh();
    }

    private int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
