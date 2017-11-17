package com.vip001.bubblebar.view;

/**
 * Created by vip001 on 2017/11/2.
 */

public interface IBubbleBarView {
    void stopAnim();

    void startSpreadAnim();

    void startShrinkAnim();

    void startEnterAnim();

    void startExitAnim();

    void setSpread();

    void setShrink();

    boolean isDark();

    void setProgress(int progress);

    void setMax(int max);

    boolean isAnimatedState();

    boolean isShrinkState();

    boolean isSpreadState();

    float getProgressRate();

    void setExitAnimRightMargin(float dp);
}
