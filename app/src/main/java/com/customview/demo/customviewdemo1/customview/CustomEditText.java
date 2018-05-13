package com.customview.demo.customviewdemo1.customview;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.EditText;

import com.customview.demo.customviewdemo1.R;

/**
 * Created by CC on 2018/5/13.
 * 自定义继承现有的edittext
 */

public class CustomEditText extends AppCompatEditText {

    private Drawable mDelete;

    public CustomEditText(Context context) {
        this(context,null);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        mDelete = getResources().getDrawable(R.mipmap.edt_delete_all);
        //设置图标的位置
        setDeleteDeawable();
        //添加输入监听
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setDeleteDeawable();
            }
        });
    }

    private void setDeleteDeawable() {
        setCompoundDrawablesWithIntrinsicBounds(null,null,length()>0?mDelete:null,null);
    }
    //重写EditTextonTouchEvent


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()==MotionEvent.ACTION_UP){
            if (mDelete != null) {
                int rawX = (int) event.getRawX();
                int rawY = (int) event.getRawY();
                //获取当前view的位置
                Rect rect = new Rect();
                getGlobalVisibleRect(rect);
                Log.d("xxx","rect1=="+rect);
                rect.left=rect.right-getPaddingRight()-50;
                Log.d("xxx","rect2=="+rect);
                if(rect.contains(rawX,rawY)){
                    setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }
}
