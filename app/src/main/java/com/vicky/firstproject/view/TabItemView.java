package com.vicky.firstproject.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vicky.firstproject.R;

/**
 * tab 子项布局
 *
 * Created by yao.cui on 2016/6/17.
 */
public class TabItemView extends LinearLayout {
    private ImageView mIconView;
    private TextView mTitleView;

    public TabItemView(Context context) {
        super(context);
        initView(context);
    }
    public TabItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public TabItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setHorizontalGravity(VERTICAL);
        mIconView = new ImageView(context);
        LinearLayout.LayoutParams iconParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        mIconView.setLayoutParams(iconParams);
        mTitleView = new TextView(context);
        mTitleView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        titleParams.gravity = Gravity.CENTER;
        mTitleView.setLayoutParams(titleParams);
        addView(mIconView);
        addView(mTitleView);
    }

    public void setIconView(Bitmap bitmap){
        if (bitmap != null&& !bitmap.isRecycled()){
            mIconView.setImageBitmap(bitmap);
        }

    }

    public void setTitleView(String title){
        mTitleView.setText(title);
    }

    public void setTextColor(int color){
        mTitleView.setTextColor(color);
    }

    /**
     * 设置字体大小单位px
     *
     * @param size
     */
    public void setTextSize(int size){
        mTitleView.setTextSize(size);
    }


}
