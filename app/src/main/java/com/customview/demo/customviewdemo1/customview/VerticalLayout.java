package com.customview.demo.customviewdemo1.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * @ Creator     :     chenchao
 * @ CreateDate  :     2018/5/11 0011 17:41
 * @ Description :     自定义垂直的linealyout
 */

public class VerticalLayout extends ViewGroup {
    private final Context mContext;

    public VerticalLayout(Context context) {
        this(context, null);
    }

    public VerticalLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }
    //测量


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取子viewgroup的大小和模式
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        //获取viewGroupd的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int paddingTop = getPaddingTop();
        //1.测量所有子view
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        //2.测量viewGroup自身大小
        int width = 0;
        int height = 0;
        //如果viewGroup的width 的测量模式是wrap_content。
        // 则需要自定义大小,找出最大的子view的宽度就是viewgroup的宽度
        if (widthMode == MeasureSpec.AT_MOST) {
            int maxWidth = 0;
            //获取所有的子view
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) childAt.getLayoutParams();
                //这里需要把子view的marging计算进去
                int childWidth = getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                //给maxwith赋值
                maxWidth = childWidth > maxWidth ? childWidth : maxWidth;
            }
            width = maxWidth + paddingLeft + paddingRight;
        }
        //如果viewGroup的heigth的测量模式是wrap_content
        if (heightMode == MeasureSpec.AT_MOST) {
            //如果需要自定义大小，垂直放置需要所有子view的高度加上子view的margin值最后加上viewgroup的padding
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (childAt.getVisibility() == GONE) {
                    continue;
                }
                MarginLayoutParams lp = (MarginLayoutParams) childAt.getLayoutParams();
                height += getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            }
            //viewGroup的高度还需要加上自身的margin
            height += paddingBottom + paddingTop;
        }
        //设置viewGroup的宽高
        setMeasuredDimension(widthMode == MeasureSpec.AT_MOST ? width : widthSize,
                heightMode == MeasureSpec.AT_MOST ? height : heightSize);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int paddingRight = getPaddingRight();
        int paddingLeft = getPaddingLeft();
        //定义child的顶点
        int childTop = 0;
        int childBottom = 0;
        int childRight = 0;
        int childLeft = 0;
        int bm = 0;
        //遍历所有的子view
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            //只有子view没有gone掉的时候才去layout
            if (childAt.getVisibility() != GONE) {
                //计算子view的margin值
                MarginLayoutParams lp = (MarginLayoutParams) childAt.getLayoutParams();
                childTop += lp.topMargin;
                childBottom += childAt.getMeasuredHeight() + lp.topMargin;
                childLeft = lp.leftMargin;
                childRight = childAt.getMeasuredWidth() + lp.leftMargin;
                //计算一个测量一个
                childAt.layout(childLeft + paddingLeft, childTop + paddingTop + bm, childRight + paddingRight, childBottom + paddingTop + bm);
                //计算下一个之前需要加上bottom(摆放第二个子view的时候才需要吧bottomMargin计算进去)
                bm += lp.bottomMargin;
                childTop += childAt.getMeasuredHeight();
            }

        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(mContext,attrs);
    }
}
