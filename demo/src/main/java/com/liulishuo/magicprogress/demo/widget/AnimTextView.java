package com.liulishuo.magicprogress.demo.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import java.lang.ref.WeakReference;

import cn.dreamtobe.percentsmoothhandler.ISmoothTarget;
import cn.dreamtobe.percentsmoothhandler.SmoothHandler;

/**
 * Created by Jacksgong on 12/11/15.
 */
public class AnimTextView extends TextView implements ISmoothTarget {
    private SmoothHandler smoothHandler;

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

    private int progress;
    private int max = 100;

    public void setProgress(final int progress) {
        if (smoothHandler != null) {
            smoothHandler.commitPercent(progress / (float) getMax());
        }

        this.progress = progress;
        setText(String.valueOf(progress));
    }

    public int getProgress() {
        return this.progress;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public float getPercent() {
        return getProgress() / (float) getMax();
    }

    @Override
    public void setPercent(float percent) {
        setProgress((int) Math.ceil(percent * getMax()));
    }

    @Override
    public void setSmoothPercent(float percent) {
        if (smoothHandler == null) {
            smoothHandler = new SmoothHandler(new WeakReference<ISmoothTarget>(this));
        }

        smoothHandler.loopSmooth(percent);
    }
}
