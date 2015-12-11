package com.liulishuo.magicprogress.library.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;

import com.liulishuo.magicprogress.library.R;


/**
 * Created by Jacksgong on 12/8/15.
 * <p/>
 * 轻量的ProgressBar
 */
public class MagicProgressBar extends View {

    @ColorInt
    private int fillColor;

    @ColorInt
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

    public float getPercent() {
        return this.percent;
    }

    public void setPercent(@FloatRange(from = 0.0, to = 1.0) final float percent) {
        this.percent = percent;
        invalidate();
    }

    private final RectF rectF = new RectF();
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
        if (fillColor != 0) {
            rectF.right = fillWidth;
            canvas.drawRoundRect(rectF, radius, radius, fillPaint);
        }

        canvas.restore();
    }
}
