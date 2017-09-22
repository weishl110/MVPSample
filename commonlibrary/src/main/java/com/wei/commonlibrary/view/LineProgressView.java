package com.wei.commonlibrary.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.example.commonlibrary.R;
import com.wei.commonlibrary.utils.LogUtil;

/**
 * Created by ${wei} on 2017/5/16.
 */

public class LineProgressView extends View {

    private Paint mStrokePaint;
    private Paint mNomalPaint;
    private Paint mProgressPaint;

    private RectF mBoxRectF;
    private RectF mNomalRectF;
    private RectF mProgressRectF;

    private final String COLOR_YELLOW = "#f39800";
    private final String COLOR_GRAY = "#666666";
    private final String COLOR_WHITE = "#ffffff";

    private float mRadius;
    private float mLineWidth;
    private Context mContext;
    private final float strokeWidth = 3;

    public LineProgressView(Context context) {
        this(context, null);
    }

    public LineProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {
        //默认进度画笔
        mNomalPaint = new Paint();
        mNomalPaint.setAntiAlias(true);
        mNomalPaint.setColor(Color.parseColor(COLOR_GRAY));
        mNomalPaint.setStyle(Paint.Style.FILL);
        mNomalPaint.setStrokeJoin(Paint.Join.ROUND);
        mNomalPaint.setStrokeCap(Paint.Cap.ROUND);
        //进度条的画笔
        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(Color.parseColor(COLOR_YELLOW));
        mProgressPaint.setStyle(Paint.Style.FILL);
        mProgressPaint.setStrokeJoin(Paint.Join.ROUND);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        //外框的画笔
        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setColor(Color.parseColor(COLOR_WHITE));
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setStrokeWidth(strokeWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        float mWidth = w - getPaddingLeft() - getPaddingRight() - strokeWidth * 2;
        float mHeight = h - getPaddingTop() - getPaddingBottom() - strokeWidth * 2;
        //进度条的矩形，在线条的里面，所以还得减去外框的宽度
        mLineWidth = mWidth - strokeWidth * 2;
        mBoxRectF = new RectF();
        mBoxRectF.left = getPaddingLeft() + strokeWidth;
        mBoxRectF.top = getPaddingTop()+strokeWidth;
        mBoxRectF.right = mBoxRectF.left + mWidth;
        mBoxRectF.bottom = mBoxRectF.top + mHeight;

        mNomalRectF = new RectF();
        mNomalRectF.left = mBoxRectF.left + strokeWidth;
        mNomalRectF.top = mBoxRectF.top + strokeWidth;
        mNomalRectF.right = mBoxRectF.right - strokeWidth;
        mNomalRectF.bottom = mBoxRectF.bottom - strokeWidth;
        //设置画笔宽度
        float lineHeight = mHeight - strokeWidth * 2;
        mNomalPaint.setStrokeWidth(lineHeight);
        mProgressPaint.setStrokeWidth(lineHeight);

        //计算圆角的半径为高度的1/4
        mRadius = mHeight / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制边框
        canvas.drawRoundRect(mBoxRectF, mRadius, mRadius, mStrokePaint);
        //进度条的默认状态
        canvas.drawRoundRect(mNomalRectF, mRadius, mRadius, mNomalPaint);
        //绘制进度状态 不断的改变右边的值
        if (mProgressRectF != null) {
            canvas.drawRoundRect(mProgressRectF, mRadius, mRadius, mProgressPaint);
        }
    }

    /**
     * 0~1
     *
     * @param currProgress
     */
    public void setPercent(float currProgress) {
        if (mProgressRectF == null) {
            mProgressRectF = new RectF();
            mProgressRectF.left = mNomalRectF.left;
            mProgressRectF.top = mNomalRectF.top;
            mProgressRectF.bottom = mNomalRectF.bottom;
        }
        if (currProgress > 1) {
            currProgress = 1;
        }
        float right = mProgressRectF.left + currProgress * mLineWidth;
        mProgressRectF.right = right;
        postInvalidate();
    }

    private float dp2px(float dpValue) {
        float density = mContext.getResources().getDisplayMetrics().density;
        return dpValue * density + 0.5f;
    }
}
