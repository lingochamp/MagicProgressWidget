/*
 * Copyright (c) 2015 LingoChamp Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.liulishuo.magicprogresswidget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Jacksgong on 12/8/15.
 * <p/>
 * 轻量的ProgressBar
 */
public class MagicProgressBar extends View {

    // ColorInt
    private int fillColor;

    // ColorInt
    private int backgroundColor;

    private Paint fillPaint;
    private Paint backgroundPaint;

    private float percent;

    public MagicProgressBar(Context context) {
        super(context);
        init(context, null);
    }

    public MagicProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MagicProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MagicProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        if (context == null || attrs == null) {
            return;
        }


        TypedArray typedArray = null;
        try {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicProgressBar);
            percent = typedArray.getFloat(R.styleable.MagicProgressBar_mpb_percent, 0);
            fillColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_color, 0);
            backgroundColor = typedArray.getColor(R.styleable.MagicProgressBar_mpb_default_color, 0);
        } finally {
            if (typedArray != null) {
                typedArray.recycle();
            }
        }

        fillPaint = new Paint();
        fillPaint.setColor(fillColor);
        fillPaint.setAntiAlias(true);

        backgroundPaint = new Paint();
        backgroundPaint.setColor(backgroundColor);
        backgroundPaint.setAntiAlias(true);

    }

    /**
     * @param fillColor ColorInt
     */
    public void setFillColor(final int fillColor) {
        if (this.fillColor != fillColor) {
            this.fillColor = fillColor;
            this.fillPaint.setColor(fillColor);
            invalidate();
        }

    }

    /**
     * @param backgroundColor ColorInt
     */
    public void setBackgroundColor(final int backgroundColor) {
        if (this.backgroundColor != backgroundColor) {
            this.backgroundColor = backgroundColor;
            this.backgroundPaint.setColor(backgroundColor);
            invalidate();
        }
    }

    public int getFillColor() {
        return this.fillColor;
    }

    public int getBackgroundColor() {
        return this.backgroundColor;
    }

    public float getPercent() {
        return this.percent;
    }

    /**
     * @param percent FloatRange(from = 0.0, to = 1.0)
     */
    public void setPercent(final float percent) {
        this.percent = percent;
        invalidate();
    }

    private final RectF rectF = new RectF();
    private final Path fillPath = new Path();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float drawPercent = percent;

        canvas.save();
        final int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        final int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

        final int fillWidth = (int) (drawPercent * width);
        final float radius = height / 2.0f;


        rectF.left = 0;
        rectF.top = 0;
        rectF.right = width;
        rectF.bottom = height;

        // draw background
        if (backgroundColor != 0) {
            canvas.drawRoundRect(rectF, radius, radius, backgroundPaint);
        }


        // draw fill
        if (fillColor != 0 && fillWidth > 0) {
            float currentX = drawPercent * width;

            if (radius > currentX / 2) {

                fillPath.reset();
                // 180 * percent
                float targetArc = 180 * currentX / radius;
                float delta = (180 - targetArc) / 2;
                rectF.right = radius * 2;
                fillPath.addArc(rectF, delta + 90, targetArc);
                fillPath.close();
                canvas.drawPath(fillPath, fillPaint);

            } else {
                rectF.right = fillWidth;
                canvas.drawRoundRect(rectF, radius, radius, fillPaint);
            }
        }

        canvas.restore();
    }
}
