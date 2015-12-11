package com.liulishuo.magicprogress.demo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Jacksgong on 12/11/15.
 */
public class AnimTextView extends TextView {
    public AnimTextView(Context context) {
        super(context);
    }

    public AnimTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnimTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private int score;

    public void setScore(final int score) {
        this.score = score;
        setText(String.valueOf(score));
    }

    public int getScore() {
        return this.score;
    }
}
