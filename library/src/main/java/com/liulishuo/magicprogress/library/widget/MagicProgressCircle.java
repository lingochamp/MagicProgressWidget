package com.liulishuo.magicprogress.library.widget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.FloatRange;
import android.util.AttributeSet;
import android.view.View;

import com.liulishuo.magicprogress.library.R;


/**
 * Created by Jacksgong on 12/8/15.
 */
public class MagicProgressCircle extends View {

    @ColorInt
    private int startColor;
    @ColorInt
    private int endColor;
    @ColorInt
    private int defaultColor;

    private int percentEndColor;

    private int strokeWidth;
    private float percent;

    // 用于渐变
    private Paint paint;


    public MagicProgressCircle(Context context) {
        super(context);
        init(context, null);
    }

    public MagicProgressCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public MagicProgressCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MagicProgressCircle(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }


    private void init(final Context context, final AttributeSet attrs) {
        float defaultPercent = -1;
        if (isInEditMode()) {
            defaultPercent = 0.6f;
        }

        do {
            final int strokeWdithDefaultValue = (int) (18 * getResources().getDisplayMetrics().density + 0.5f);
            if (context == null || attrs == null) {
                strokeWidth = strokeWdithDefaultValue;
                percent = defaultPercent;
                startColor = getResources().getColor(R.color.mpc_start_color);
                endColor = getResources().getColor(R.color.mpc_end_color);
                defaultColor = getResources().getColor(R.color.mpc_default_color);
                break;
            }

            TypedArray typedArray = null;
            try {
                typedArray = context.obtainStyledAttributes(attrs, R.styleable.MagicProgressCircle);
                percent = typedArray.getFloat(R.styleable.MagicProgressCircle_mpc_percent, defaultPercent);
                strokeWidth = (int) typedArray.getDimension(R.styleable.MagicProgressCircle_mpc_stroke_width, strokeWdithDefaultValue);
                startColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_start_color, getResources().getColor(R.color.mpc_start_color));
                endColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_end_color, getResources().getColor(R.color.mpc_end_color));
                defaultColor = typedArray.getColor(R.styleable.MagicProgressCircle_mpc_default_color, getResources().getColor(R.color.mpc_default_color));
            } finally {
                if (typedArray != null) {
                    typedArray.recycle();
                }
            }


        } while (false);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        startPaint = new Paint();
        startPaint.setColor(startColor);
        startPaint.setAntiAlias(true);
        startPaint.setStyle(Paint.Style.FILL);


        endPaint = new Paint();
        endPaint.setAntiAlias(true);
        endPaint.setStyle(Paint.Style.FILL);

        int endR = (endColor & 0xFF0000) >> 16;
        int endG = (endColor & 0xFF00) >> 8;
        int endB = (endColor & 0xFF);

        this.startR = (startColor & 0xFF0000) >> 16;
        this.startG = (startColor & 0xFF00) >> 8;
        this.startB = (startColor & 0xFF);

        deltaR = endR - startR;
        deltaG = endG - startG;
        deltaB = endB - startB;

        calculatePercentEndColor(percent);
        endPaint.setColor(percentEndColor);


        customColors = new int[]{startColor, percentEndColor, defaultColor, defaultColor};
        fullColors = new int[]{startColor, endColor};
        emptyColors = new int[]{defaultColor, defaultColor};

        customPositions = new float[4];
        customPositions[0] = 0;
        customPositions[3] = 1;

        extremePositions = new float[]{0, 1};
    }

    public void setPercent(@FloatRange(from = 0.0, to = 1.0) final float percent) {
        this.percent = percent;
        calculatePercentEndColor(percent);
        endPaint.setColor(percentEndColor);

        invalidate();
    }

    public float getPercent() {
        return this.percent;
    }

    private int deltaR, deltaB, deltaG;
    private int startR, startB, startG;

    private void calculatePercentEndColor(final float percent) {
        percentEndColor = ((int) (deltaR * percent + startR) << 16) +
                ((int) (deltaG * percent + startG) << 8) +
                ((int) (deltaB * percent + startB)) + 0xFF000000;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        this.rectF.left = getMeasuredWidth() / 2 - strokeWidth / 2;
        this.rectF.top = 0;
        this.rectF.right = getMeasuredWidth() / 2 + strokeWidth / 2;
        this.rectF.bottom = strokeWidth;
    }

    private Paint startPaint;
    private Paint endPaint;


    private final RectF rectF = new RectF();

    private int[] customColors;
    private int[] fullColors;
    private int[] emptyColors;
    private float[] customPositions;
    private float[] extremePositions;


    // 目前由于SweepGradient赋值只在构造函数，无法pre allocate & reuse instead
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int restore = canvas.save();

        final int cx = getMeasuredWidth() / 2;
        final int cy = getMeasuredHeight() / 2;
        final int radius = getMeasuredWidth() / 2 - strokeWidth / 2;

        float drawPercent = percent;
        if (drawPercent > 0.97 && drawPercent < 1) {
            // 设计师说这样比较好
            drawPercent = 0.97f;
        }

        // 画渐变圆
        canvas.save();
        canvas.rotate(-90, cx, cy);
        int[] colors;
        float[] positions;
        if (drawPercent < 1 && drawPercent > 0) {
            calculatePercentEndColor(drawPercent);
            customColors[1] = percentEndColor;
            colors = customColors;
            customPositions[1] = drawPercent;
            customPositions[2] = drawPercent;
            positions = customPositions;
        } else if (drawPercent == 1) {
            colors = fullColors;
            positions = extremePositions;
        } else {
            // <= 0 || > 1?
            colors = emptyColors;
            positions = extremePositions;
        }
        final SweepGradient sweepGradient = new SweepGradient(getMeasuredWidth() / 2, getMeasuredHeight() / 2, colors, positions);
        paint.setShader(sweepGradient);
        canvas.drawCircle(cx, cy, radius, paint);
        canvas.restore();

        if (drawPercent > 0) {

            // 绘制结束的半圆
            if (drawPercent < 1) {
                canvas.save();
                canvas.rotate((int) Math.floor(360.0f * drawPercent) - 1, cx, cy);
                canvas.drawArc(rectF, -90f, 180f, true, endPaint);
                canvas.restore();
            }


            canvas.save();
            // 绘制开始的半圆
            canvas.drawArc(rectF, 90f, 180f, true, startPaint);
            canvas.restore();

        }


        canvas.restoreToCount(restore);

    }
}
