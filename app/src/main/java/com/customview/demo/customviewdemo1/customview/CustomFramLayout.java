package com.customview.demo.customviewdemo1.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.customview.demo.customviewdemo1.R;

/**
 * Created by CC on 2018/5/13.
 */

public class CustomFramLayout extends FrameLayout {
    private  Context mContext;
    private String mLeftText;
    private String mRightText;
    private Drawable mLeftImag;
    private int mRightImag;
    private TextView mTxt_left;
    private TextView mTxt_right;
    private ImageView mImg_right;

    public CustomFramLayout(@NonNull Context context) {
        this(context,null);
    }

    public CustomFramLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext=context;
        initAttrs(attrs);
        initView();
        initData();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.item_layout,this);
        mTxt_left = (TextView) findViewById(R.id.txt_left);
        mTxt_right = (TextView) findViewById(R.id.txt_right);
        mImg_right = (ImageView) findViewById(R.id.img_right);
    }

    private void initData() {
        if (mTxt_left != null) {
            mTxt_left.setText(mLeftText);
        }
        if (mTxt_right != null) {
            mTxt_right.setText(mRightText);
        }
        if (mImg_right != null) {
            mImg_right.setImageResource(mRightImag);
        }

    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.CustomFramLayout);
        mLeftText = typedArray.getString(R.styleable.CustomFramLayout_item_leftText);
        mRightText = typedArray.getString(R.styleable.CustomFramLayout_item_rightText);
        mLeftImag = typedArray.getDrawable(R.styleable.CustomFramLayout_item_leftImag);
        mRightImag = typedArray.getResourceId(R.styleable.CustomFramLayout_item_rightImag, R.mipmap.btn_arrow_white);
        typedArray.recycle();
    }
}
