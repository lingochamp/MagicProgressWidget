package com.liulishuo.magicprogress.library.util;

import android.content.Context;

/**
 * Created by Jacksgong on 5/27/15.
 */
public class ViewUtil {


    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            // may be context destroyed by activity, but still invoke by fragment
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
