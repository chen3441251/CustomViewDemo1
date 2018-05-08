package com.customview.demo.customviewdemo1.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * @ Creator     :     chenchao
 * @ CreateDate  :     2018/5/8 0008 17:25
 * @ Description :     CustomViewDemo1
 */

public class CicleView extends View {

    private Paint mPaint;
    private float mCenterX;
    private float mCenterY;
    private float mRadius = 200;

    public CicleView(Context context) {
        this(context, null);
    }

    public CicleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = (int) (mRadius * 2 + getPaddingLeft() + getPaddingRight());
        }
        if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = (int) (mRadius * 2 + getPaddingBottom() + getPaddingTop());
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //获取padding属性
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        //计算圆心的坐标
        mCenterX = (w - paddingLeft - paddingRight) / 2 + paddingLeft;
        mCenterY = (h - paddingBottom - paddingTop) / 2 + paddingTop;
        Log.d("xxx", "onSizeChanged==" + w);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("xxx", "onDraw==" + getWidth());
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
    }
}
