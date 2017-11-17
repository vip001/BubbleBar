package com.vip001.bubblebar.view;

/**
 * Created by vip001 on 2017/11/17.
 */

public class UIUtils {
    private static final float DENSITY;
    static {
        DENSITY = BrothersApplication.getApplicationInstance().getResources().getDisplayMetrics().density;
    }
    public static int dip2ipx(float dp) {
        return (int) (DENSITY * dp + 0.5f);
    }

    public static float dip2fpx(float dp) {
        return DENSITY * dp;
    }
}
