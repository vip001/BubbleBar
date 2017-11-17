package com.vip001.bubblebar.sample;

/**
 * Created by vip001 on 2017/11/6.
 */

public class ShowRunnableState extends BaseState {
    /**
     * 初始化标记
     */
    public static final int STATE_INITIAL = 1 << 1;

    public static final int STATE_EXIT = 1 <<2;

    public static int STATE_CLEAR=1<<3;
    public static int STATE_HAS_SHOW=1<<4;

    {
        mState = STATE_INITIAL;
    }

}
