package com.vip001.bubblebar.sample;

import android.os.Handler;
import android.os.SystemClock;

/**
 * Created by vip001 on 2017/11/3.
 */

public abstract class ScheduleTask implements Runnable {
    private long mTime;

    private long mDelay;

    private Handler mHandler;

    private boolean mStop;

    {
        mTime = 0;
        mStop = false;
    }

    /**
     * 构造
     * @param delay
     * @param handler
     */
    public ScheduleTask(long delay, Handler handler) {
        assert delay>=0||handler!=null;
        this.mDelay = delay;
        this.mHandler = handler;
    }

    /**
     * 停止
     */
    public void stop() {
        mStop = true;
        mHandler.removeCallbacks(this);
    }

    /**
     * 开始
     */
    public void start() {
        mStop = false;
        mHandler.postDelayed(this, mDelay);
    }

    /**
     * 重置
     */
    public void reset() {
        mStop = false;
        mTime = 0;
    }
    /**
     * run
     */
    @Override
    public void run() {
        if (mStop) {
            return;
        }
        if (SystemClock.uptimeMillis() - mTime < mDelay) {
            mHandler.postDelayed(this,mDelay - (SystemClock.uptimeMillis() - mTime));
            return;
        }
        mTime = SystemClock.uptimeMillis();
        if(execute()){
            mStop=true;
        }else{
            mHandler.postDelayed(this, mDelay);
        }

    }

    /**
     * 执行
     * @return
     */
    protected abstract boolean execute();
}
