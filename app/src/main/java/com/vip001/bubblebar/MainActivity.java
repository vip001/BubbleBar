package com.vip001.bubblebar;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import com.vip001.bubblebar.sample.ScheduleTask;
import com.vip001.bubblebar.sample.ShowRunnableState;
import com.vip001.bubblebar.view.BubbleBarView;
import com.vip001.bubblebar.view.BubbleTipBar;

public class MainActivity extends Activity {
    private BubbleBarView mBubbleBarView;
    private ScheduleTask mBubbleBarShowTask;
    private ShowRunnableState mBubbleBarShowState;
    private ScheduleTask mBubbleTipBarProgressTask;
    private ScheduleTask mBubbleBarProgressTask;

    private BubbleTipBar mBubbleTipBar;
    private ScheduleTask mBubbleTipBarShowTask;
    private ShowRunnableState mBubbleTipBarShowState;
    private ScheduleTask mBubbleTipBarQuitTask;
    private Handler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initBubblebarView();
        initBubbleTipBar();
        initBtn();
    }

    private void init() {
        mHandler = new Handler(Looper.getMainLooper());
        mBubbleTipBarQuitTask = new ScheduleTask(5000, mHandler) {
            @Override
            protected boolean execute() {
                mBubbleTipBar.startExitAnim();
                return true;
            }
        };
        mBubbleTipBarShowTask = new ScheduleTask(5000, mHandler) {
            @Override
            protected boolean execute() {
                if (!mBubbleTipBar.isAnimatedState() && mBubbleTipBar.isSpreadState()) {
                    mBubbleTipBar.startShrinkAnim();
                }
                return true;
            }
        };
        mBubbleTipBarShowState = new ShowRunnableState();


        mBubbleTipBarProgressTask = new ScheduleTask(1000, mHandler) {
            private int progress = 0;

            @Override
            protected boolean execute() {
                progress += 10;
                if (progress > 100) {
                    progress = 0;
                }
                mBubbleTipBar.setProgress(progress);
                return false;
            }
        };
        mBubbleBarShowTask = new ScheduleTask(5000, mHandler) {
            @Override
            protected boolean execute() {
                if (!mBubbleBarView.isAnimatedState() && mBubbleBarView.isSpreadState()) {
                    mBubbleBarView.startShrinkAnim();
                }
                return true;
            }
        };
        mBubbleBarShowState = new ShowRunnableState();
        mBubbleBarProgressTask = new ScheduleTask(1000, mHandler) {
            private int progress = 0;

            @Override
            protected boolean execute() {
                progress += 10;
                if (progress > 100) {
                    progress = 0;
                }
                mBubbleBarView.setProgress(progress);
                return false;
            }
        };
    }

    private void initBtn() {
        findViewById(R.id.btn_spread).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bubbleTipBarEnter();
                mBubbleTipBarProgressTask.start();
            }
        });
        findViewById(R.id.btn_shrink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBubbleTipBar.stopAnim();
            }
        });
        findViewById(R.id.btn_black).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getWindow().getDecorView().setBackgroundColor(Color.BLACK);
            }
        });
        findViewById(R.id.btn_white).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.getWindow().getDecorView().setBackgroundColor(Color.WHITE);
            }
        });
    }

    private void initBubbleTipBar() {
        mBubbleTipBar = (BubbleTipBar) findViewById(R.id.bubbleTipBar);
        mBubbleTipBar.setFirstText("+817kb/s");
        mBubbleTipBar.setSecondText("剩余1024M");
        mBubbleTipBar.setExitAnimRightMargin(10);
        mBubbleTipBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBubbleTipBar.isDark()) {
                    Toast.makeText(MainActivity.this, "触发点击", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mBubbleTipBar.isAnimatedState()) {
                    if (mBubbleTipBarShowState.hasState(ShowRunnableState.STATE_CLEAR)) {
                        mBubbleTipBarShowTask.stop();
                        mBubbleTipBarShowState.setState(ShowRunnableState.STATE_CLEAR);
                    }
                    if (mBubbleTipBar.isShrinkState()) {
                        mBubbleTipBar.startSpreadAnim();
                    } else if (mBubbleTipBar.isSpreadState()) {
                        mBubbleTipBar.startShrinkAnim();
                    }
                }
            }
        });
        mBubbleTipBar.setCallBack(new BubbleTipBar.BubbleTipBarCallbackImpl() {

            @Override
            public void onEnterBegin(BubbleBarView view) {

            }

            @Override
            public void onEnterFinish(BubbleBarView view) {
                mBubbleTipBar.startSpreadAnim();
                if (mBubbleTipBarShowState.hasState(ShowRunnableState.STATE_HAS_SHOW)) {
                    mBubbleTipBarShowTask.start();
                    mBubbleTipBarShowState.setState(ShowRunnableState.STATE_HAS_SHOW);
                }
            }

            @Override
            public void onExitBegin(BubbleBarView view) {

            }

            @Override
            public void onSpreadFinish(BubbleBarView view) {
                if (mBubbleTipBarShowState.hasState(ShowRunnableState.STATE_EXIT)) {
                    mBubbleTipBarQuitTask.start();
                }
            }

            @Override
            public void onExitFinish(BubbleBarView view) {
                if (mBubbleTipBarShowState.hasState(ShowRunnableState.STATE_EXIT)) {
                    mBubbleTipBarShowState.clearState(ShowRunnableState.STATE_EXIT);
                }
                mBubbleTipBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onBecomeDark(BubbleBarView view) {
                mBubbleTipBar.setFirstTextColor(Color.WHITE);
                mBubbleTipBar.setSecondTextColor(Color.argb(255, 35, 159, 253));
                mBubbleTipBar.setFirstText("试用已结束");
                mBubbleTipBar.setSecondText("开通会员>>");
                mBubbleTipBarProgressTask.stop();
                //退场处理
                bubbleTipExit();


            }

            @Override
            public void onBecomeLight(BubbleBarView view) {
                mBubbleTipBar.setFirstTextColor(Color.argb(255, 35, 159, 253));
                mBubbleTipBar.setSecondTextColor(Color.WHITE);
                mBubbleTipBar.setFirstText("+817kb/s");
                mBubbleTipBar.setSecondText("剩余1024M");
            }

        });
        bubbleTipBarEnter();
        mBubbleTipBarProgressTask.start();

    }

    private void bubbleTipExit() {
        if (mBubbleTipBarShowState.hasState(ShowRunnableState.STATE_EXIT)) {
            return;
        }
        mBubbleTipBarShowState.setState(ShowRunnableState.STATE_EXIT);
        if (mBubbleTipBar.isAnimatedState()) {
            mBubbleTipBar.stopAnim();
            mBubbleTipBar.startSpreadAnim();

        } else {
            if (mBubbleTipBar.isShrinkState()) {
                mBubbleTipBar.startSpreadAnim();
            } else {
                mBubbleTipBarQuitTask.start();
            }

        }
    }

    private void bubbleTipBarEnter() {
        mBubbleTipBar.setShrink();
        mBubbleTipBar.startEnterAnim();
    }

    private void bubbleBarEnter() {
        mBubbleBarView.setShrink();
        mBubbleBarView.startEnterAnim();
    }

    private void initBubblebarView() {
        mBubbleBarView = (BubbleBarView) findViewById(R.id.pulltailerview);
        mBubbleBarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBubbleBarView.isDark()) {
                    return;
                }
                if (!mBubbleBarView.isAnimatedState()) {
                    if (mBubbleBarShowState.hasState(ShowRunnableState.STATE_CLEAR)) {
                        mBubbleBarShowTask.stop();
                        mBubbleBarShowState.setState(ShowRunnableState.STATE_CLEAR);
                    }
                    if (mBubbleBarView.isShrinkState()) {
                        mBubbleBarView.startSpreadAnim();
                    } else if (mBubbleBarView.isSpreadState()) {
                        mBubbleBarView.startShrinkAnim();

                    }
                }
            }
        });

        mBubbleBarView.setCallback(new BubbleBarView.BubbleBarViewCallbackImpl() {
            @Override
            public void onEnterFinish(BubbleBarView view) {
                mBubbleBarView.startSpreadAnim();
                if (mBubbleBarShowState.hasState(ShowRunnableState.STATE_HAS_SHOW)) {
                    mBubbleBarShowTask.start();
                    mBubbleBarShowState.setState(ShowRunnableState.STATE_HAS_SHOW);
                }

            }
        });
        bubbleBarEnter();
        mBubbleBarProgressTask.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacksAndMessages(null);
    }
}
