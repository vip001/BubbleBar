package com.vip001.bubblebar.sample;

/**
 * Created by vip001 on 2017/11/13.
 */

public class BaseState {
    protected int mState;

    /**
     * 添加状态
     * @param state
     */
    public void setState(int state) {
       if(checkState(state)){
            mState = mState & ~state | state;
        }

    }

    /**
     * 状态判断
     * @param state
     * @return
     */
    public boolean hasState(int state) {
        return (mState & state) == state;
    }

    /**
     * 清除状态
     * @param state
     */
    public void clearState(int state) {
        if(checkState(state)){
            mState = mState & ~state;
        }

    }

    /**
     * 检查状态
     * @param state
     */
    protected boolean checkState(int state) {
        return state%2==0;
    }
}
