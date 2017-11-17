package com.vip001.bubblebar.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * Created by vip001 on 2017/11/2.
 */

public class BubbleTipBar extends FrameLayout implements IBubbleBarView {
    private BubbleBarView mBubbleBarView;
    private TextView mFirstText;
    private TextView mSecondText;
    private BubbleTipBarCallback mCallback;
    private LinearLayout mTextContainer;

    public BubbleTipBar( Context context) {
        super(context);
        initView();
    }
    public BubbleTipBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }
    public BubbleTipBar(Context context,AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }
    @TargetApi(21)
    public BubbleTipBar(Context context,AttributeSet attrs, int defStyleAttr,int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }
    public void setCallBack(BubbleTipBarCallback callBack) {
        this.mCallback = callBack;
    }
    public void setFirstTextColor(int color) {
        mFirstText.setTextColor(color);
    }
    public void setSecondTextColor(int color) {
        mSecondText.setTextColor(color);
    }
    public void setFirstText(String text) {
        mFirstText.setText(text);
    }

    public void setSecondText(String text) {
        mSecondText.setText(text);
    }

    public void setFirstTextClickListener(OnClickListener listener) {
        mFirstText.setOnClickListener(listener);
    }

    public void setSecondTextClickListener(OnClickListener listener) {
        mSecondText.setOnClickListener(listener);
    }

    private void initView() {
        mBubbleBarView = new BubbleBarView(getContext());
        mBubbleBarView.setCallback(new BubbleBarView.BubbleBarViewCallback() {
            @Override
            public void onEnterBegin(BubbleBarView view) {
                if (mCallback != null) {
                    mCallback.onEnterBegin(view);
                }
            }

            @Override
            public void onEnterFinish(BubbleBarView view) {
                if (mCallback != null) {
                    mCallback.onEnterFinish(view);
                }
            }

            @Override
            public void onExitBegin(BubbleBarView view) {

                if (mCallback != null) {
                    mCallback.onExitBegin(view);
                }
            }

            @Override
            public void onExitFinish(BubbleBarView view) {
                BubbleTipBar.this.clearAnimation();
                BubbleTipBar.this.setVisibility(View.GONE);
                if (mCallback != null) {
                    mCallback.onExitFinish(view);
                }

            }

            @Override
            public void onSpreadBegin(BubbleBarView view) {
                mTextContainer.setVisibility(View.GONE);
                if (mCallback != null) {
                    mCallback.onSpreadBegin(view);
                }
            }

            @Override
            public void onSpreadFinish(BubbleBarView view) {
                mTextContainer.setVisibility(View.VISIBLE);
                if (mCallback != null) {
                    mCallback.onSpreadFinish(view);
                }
            }

            @Override
            public void onShrinkBegin(BubbleBarView view) {
                mTextContainer.setVisibility(View.GONE);
                if (mCallback != null) {
                    mCallback.onShrinkBegin(view);
                }
            }

            @Override
            public void onShrinkFinish(BubbleBarView view) {
                if (mCallback != null) {
                    mCallback.onShrinkFinish(view);
                }
            }

            @Override
            public void onBecomeDark(BubbleBarView view) {
                if (mCallback != null) {
                    mCallback.onBecomeDark(view);
                }
            }

            @Override
            public void onBecomeLight(BubbleBarView view) {
                if (mCallback != null) {
                    mCallback.onBecomeLight(view);
                }
            }
        });
        addView(mBubbleBarView);

        mFirstText = new TextView(getContext());
        mFirstText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        mFirstText.setTextColor(Color.argb(255, 35, 159, 253));


        mSecondText = new TextView(getContext());
        mSecondText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 8);
        mSecondText.setTextColor(Color.WHITE);


        mTextContainer = new LinearLayout(getContext());
        mTextContainer.setOrientation(LinearLayout.VERTICAL);
        mTextContainer.setVisibility(View.GONE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin =UIUtils.dip2ipx(11);

        mTextContainer.addView(mFirstText, params);
        mTextContainer.addView(mSecondText, params);

        LayoutParams flParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flParams.gravity = Gravity.CENTER_VERTICAL;
        addView(mTextContainer, flParams);

    }

    @Override
    public void stopAnim() {
        mBubbleBarView.stopAnim();
        BubbleTipBar.this.clearAnimation();
    }

    @Override
    public void startSpreadAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mBubbleBarView.startSpreadAnim();
    }

    @Override
    public void startShrinkAnim() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mBubbleBarView.startShrinkAnim();
    }

    @Override
    public void startEnterAnim() {
        mBubbleBarView.startEnterAnim(this);
    }

    @Override
    public void startExitAnim() {
        mBubbleBarView.startExitAnim(this);
    }

    @Override
    public void setSpread() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mBubbleBarView.setSpread();
    }

    @Override
    public void setShrink() {
        if (getVisibility() != View.VISIBLE) {
            this.setVisibility(View.VISIBLE);
        }
        mBubbleBarView.setShrink();
    }

    @Override
    public boolean isDark() {
        return mBubbleBarView.isDark();
    }

    @Override
    public void setProgress(int progress) {
        mBubbleBarView.setProgress(progress);
    }

    @Override
    public void setMax(int max) {
        mBubbleBarView.setMax(max);
    }

    @Override
    public boolean isAnimatedState() {
        return mBubbleBarView.isAnimatedState();
    }

    @Override
    public boolean isShrinkState() {
        return mBubbleBarView.isShrinkState();
    }

    @Override
    public boolean isSpreadState() {
        return mBubbleBarView.isSpreadState();
    }

    @Override
    public float getProgressRate() {
        return mBubbleBarView.getProgressRate();
    }

    @Override
    public void setExitAnimRightMargin(float dp) {
        mBubbleBarView.setExitAnimRightMargin(dp);
    }



    public interface BubbleTipBarCallback extends BubbleBarView.BubbleBarViewCallback {

    }

    public static abstract class BubbleTipBarCallbackImpl implements BubbleTipBarCallback {

        @Override
        public void onEnterFinish(BubbleBarView view) {

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
