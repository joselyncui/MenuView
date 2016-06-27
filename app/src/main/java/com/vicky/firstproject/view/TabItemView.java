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

    private int mTextColor;
    private int mTextSelectColor;
    private Bitmap mBmpIcon;


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

    public void setTextSelectColor(int mTextSelectColor) {
        this.mTextSelectColor = mTextSelectColor;
    }

    public void setImgBmp(Bitmap mIcon) {
        this.mBmpIcon = mIcon;
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
        mTitleView.setTextColor(mTextColor);
        mIconView.setImageBitmap(mBmpIcon);

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

    public void setTitle(String title){
        mTitleView.setText(title);
    }

    public void setTextColor(int color){
        mTextColor = color;
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

    /**
     * 根据是否被选中，修改字体颜色
     *
     * @param isSelected
     */
    public void setTextSelected(boolean isSelected){
        int color = isSelected? mTextSelectColor:mTextColor;
        mTitleView.setSelected(isSelected);
        mTitleView.setTextColor(color);
    }

    /**
     * 获取该item是否被选中
     *
     * @return Boolean
     */
    public boolean isSelected(){
        return mTitleView.isSelected();
    }


}
