package com.vip001.bubblebar.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import com.vip001.bubblebar.R;


/**
 * Created by vip001 on 2017/11/1.
 */

public class BubbleBarView extends View implements Animator.AnimatorListener, Animation.AnimationListener, IBubbleBarView {
    private static final String BACKGROUND_COLOR;
    private static final String BACKGROUND_ARC_DARK;
    private static final String BACKGROUND_ARC_LIGHT;
    private static final long DURATION_SPREAD_SHRINK;
    private static final long DURATION_ENTER_EXIT;
    private static final int DEFAULT_WIDTH;
    private static final int DEFAULT_HEIGHT;


    static {
        BACKGROUND_COLOR = "#323232";
        BACKGROUND_ARC_DARK = "#094b7d";
        BACKGROUND_ARC_LIGHT = "#239ffd";
        DURATION_SPREAD_SHRINK = 500;
        DURATION_ENTER_EXIT = 500;
        DEFAULT_WIDTH = UIUtils.dip2ipx(95f);
        DEFAULT_HEIGHT =UIUtils.dip2ipx(30);
    }
    private Bitmap mLightningBitmap;
    private Bitmap mLightningDarkBitmap;
    private Paint mBackgroundPaint;
    private Paint mArcLightPaint;
    private Paint mArcDarkPaint;
    private float mARCWidth;
    private int mWidth;
    private int mHeight;
    private int mMax = 100;
    private int mProgress = 0;
    private RectF mArcRect;
    private RectF mRoundRect;
    private ValueAnimator mSpreadAnim;
    private ValueAnimator mShrinkAnim;
    private TranslateAnimation mEnterAnim;
    private TranslateAnimation mExitAnim;
    private boolean mAnimated;
    private BubbleBarViewCallback mCallback;


    public BubbleBarView(Context context) {
        super(context);
        initView();
    }

    public BubbleBarView(Context context,AttributeSet attrs) {
        super(context, attrs);
        initView();

    }

    public BubbleBarView(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(21)
    public BubbleBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    @Override
    public boolean isDark() {
        return mProgress == mMax;
    }

    private boolean checkProgressValid(int progress) {
        return progress >= 0 && progress <= mMax;
    }

    @Override
    public void setProgress(int progress) {
        if (checkProgressValid(progress)) {
            if (isDark() && progress < mMax) {
                mCallback.onBecomeLight(this);
            }
            if (mProgress < mMax && progress == mMax && mCallback != null) {
                mCallback.onBecomeDark(this);
            }
            this.mProgress = progress;

            invalidate();
        }
    }

    @Override
    public void setMax(int max) {
        mMax = max;
    }

    public void setCallback(BubbleBarViewCallback callback) {
        this.mCallback = callback;
    }

    private void initView() {
        mAnimated = false;
        mARCWidth =UIUtils.dip2fpx(4);
        mWidth = DEFAULT_HEIGHT;
        mHeight = DEFAULT_HEIGHT;
        mLightningBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lightning);
        mLightningDarkBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.lightning_back);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(Color.parseColor(BACKGROUND_COLOR));
        mBackgroundPaint.setStyle(Paint.Style.FILL);

        mArcLightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcLightPaint.setColor(Color.parseColor(BACKGROUND_ARC_LIGHT));


        mArcDarkPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mArcDarkPaint.setColor(Color.parseColor(BACKGROUND_ARC_DARK));

        mSpreadAnim = ValueAnimator.ofFloat(0, 1).setDuration(DURATION_SPREAD_SHRINK);
        mSpreadAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rate = (float) animation.getAnimatedValue();
                mWidth = (int) ((DEFAULT_WIDTH - mHeight) * rate + mHeight);
                requestLayout();
            }
        });
        mSpreadAnim.addListener(this);

        mShrinkAnim = ValueAnimator.ofFloat(1, 0).setDuration(DURATION_SPREAD_SHRINK);
        mShrinkAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float rate = (float) animation.getAnimatedValue();
                mWidth = (int) ((DEFAULT_WIDTH - mHeight) * rate + mHeight);
                requestLayout();
            }
        });
        mShrinkAnim.addListener(this);

        mEnterAnim = new TranslateAnimation(DEFAULT_WIDTH, 0, 0, 0);
        mEnterAnim.setFillAfter(true);
        mEnterAnim.setDuration(DURATION_ENTER_EXIT);
        mEnterAnim.setAnimationListener(this);

        mExitAnim = new TranslateAnimation(0, DEFAULT_WIDTH, 0, 0);
        mExitAnim.setFillAfter(true);
        mExitAnim.setDuration(DURATION_ENTER_EXIT);
        mExitAnim.setAnimationListener(this);
    }

    @Override
    public boolean isAnimatedState() {
        return mAnimated;
    }

    @Override
    public boolean isShrinkState() {
        return mWidth == DEFAULT_HEIGHT;
    }

    @Override
    public boolean isSpreadState() {
        return mWidth > DEFAULT_HEIGHT;
    }

    @Override
    public float getProgressRate() {
        return mProgress * 1.0f / mMax;
    }

    @Override
    public void setExitAnimRightMargin(float dp) {
        int rightmargin =UIUtils.dip2ipx(dp);
        mExitAnim = new TranslateAnimation(0, DEFAULT_WIDTH + rightmargin, 0, 0);
        mExitAnim.setFillAfter(true);
        mExitAnim.setDuration(DURATION_ENTER_EXIT);
        mExitAnim.setAnimationListener(this);
    }

    @Override
    public void setSpread() {
        if (mCallback != null) {
            mCallback.onSpreadFinish(this);
        }
        mWidth = DEFAULT_WIDTH;
        requestLayout();
        if (mCallback != null) {
            mCallback.onSpreadFinish(this);
        }
    }

    @Override
    public void setShrink() {
        if (mCallback != null) {
            mCallback.onShrinkBegin(this);
        }
        mWidth = DEFAULT_HEIGHT;
        requestLayout();
        if (mCallback != null) {
            mCallback.onShrinkFinish(this);
        }
    }

    @Override
    public void stopAnim() {
        mSpreadAnim.end();
        mShrinkAnim.end();
        this.clearAnimation();
        mAnimated = false;

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mArcRect = new RectF(mWidth - mHeight, 0, mWidth, mHeight);
        mRoundRect = new RectF(0f, 0f, mWidth, mHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    public void startSpreadAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mSpreadAnim.start();
    }

    @Override
    public void startShrinkAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mShrinkAnim.start();
    }

    @Override
    public void startEnterAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        this.startAnimation(mEnterAnim);
    }

    public void startEnterAnim(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        view.startAnimation(mEnterAnim);
    }

    public void startExitAnim(View view) {
        if (view == null) {
            return;
        }
        if (view.getVisibility() != View.VISIBLE) {
            view.setVisibility(View.VISIBLE);
        }
        view.startAnimation(mExitAnim);
    }

    @Override
    public void startExitAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        this.startAnimation(mExitAnim);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(mRoundRect, mHeight / 2, mHeight / 2, mBackgroundPaint);
        canvas.drawArc(mArcRect, -90f, 360, true, mArcDarkPaint);
        canvas.drawArc(mArcRect, -90f - 360 * (1 - getProgressRate()), 360 * (1 - getProgressRate()), true, mArcLightPaint);
        if (isDark()) {
            canvas.drawBitmap(mLightningDarkBitmap, mWidth - mARCWidth - UIUtils.dip2fpx(22), mARCWidth, null);
        } else {
            canvas.drawBitmap(mLightningBitmap, mWidth - mARCWidth - UIUtils.dip2fpx(22), mARCWidth, null);
        }
    }

    @Override
    public void onAnimationStart(Animator animation) {
        mAnimated = true;
        if (mCallback != null) {
            if (animation == mSpreadAnim) {
                mCallback.onSpreadBegin(this);
            } else if (animation == mShrinkAnim) {
                mCallback.onShrinkBegin(this);
            }
        }

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mAnimated = false;
        if (mCallback != null) {
            if (animation == mSpreadAnim) {
                mCallback.onSpreadFinish(this);
            } else if (animation == mShrinkAnim) {
                mCallback.onShrinkFinish(this);
            }
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {
        mAnimated = false;
    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    @Override
    public void onAnimationStart(Animation animation) {
        mAnimated = true;
        if (mCallback != null) {
            if (animation == mEnterAnim) {
                mCallback.onEnterBegin(this);
            } else if (animation == mExitAnim) {
                mCallback.onExitBegin(this);
            }
        }


    }

    @Override
    public void onAnimationEnd(Animation animation) {
        mAnimated = false;
        if (mCallback != null) {
            if (animation == mEnterAnim) {
                mCallback.onEnterFinish(this);
            } else if (animation == mExitAnim) {
                mCallback.onExitFinish(this);
            }
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface BubbleBarViewCallback {

        void onEnterBegin(BubbleBarView view);

        void onEnterFinish(BubbleBarView view);

        void onExitBegin(BubbleBarView view);

        void onExitFinish(BubbleBarView view);

        void onSpreadBegin(BubbleBarView view);

        void onSpreadFinish(BubbleBarView view);

        void onShrinkBegin(BubbleBarView view);

        void onShrinkFinish(BubbleBarView view);

        void onBecomeDark(BubbleBarView view);

        void onBecomeLight(BubbleBarView view);


    }

    public static abstract class BubbleBarViewCallbackImpl implements BubbleBarViewCallback {

        @Override
        public void onEnterBegin(BubbleBarView view) {

        }

        @Override
        public void onEnterFinish(BubbleBarView view) {

        }

        @Override
        public void onExitBegin(BubbleBarView view) {

        }

        @Override
        public void onExitFinish(BubbleBarView view) {

        }

        @Override
        public void onSpreadBegin(BubbleBarView view) {

        }

        @Override
        public void onSpreadFinish(BubbleBarView view) {

        }

        @Override
        public void onShrinkBegin(BubbleBarView view) {

        }

        @Override
        public void onShrinkFinish(BubbleBarView view) {

        }

        @Override
        public void onBecomeDark(BubbleBarView view) {

        }

        @Override
        public void onBecomeLight(BubbleBarView view) {

        }
    }
}
