package com.vicky.firstproject.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vicky.firstproject.R;
import com.vicky.firstproject.model.TabItem;
import com.vicky.firstproject.model.TabItem;
import com.vicky.firstproject.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2016/6/17.
 */
public class MenuView extends HorizontalScrollView {
    private final String TAG = "MenuView";

    private final int DEFAULT_VISIBLE_COUNT = 3;
    private final int DEFAULT_DURATION = 300;

    private List<TabItem> mItemDatas = new ArrayList<>();
    private RelativeLayout mParentContainer;//最外层容器
    private LinearLayout mItemContainer;//tab 容器
    private ImageView mBarView;//下滑条视图
    private int mTabCount;//tab数量
    private boolean isShowBar = true;
    private int mVisibleCount;

    private OnTabItemClickListener mTabItemClickListenr;
    private ObjectAnimator mBarMoveAnimator;

    private int mDefaultPaddingTB = DensityUtil.dip2px(getContext(),3);
    private int mDefaultPaddingLR = DensityUtil.dip2px(getContext(),3);

    private int mBarHeight = DensityUtil.dip2px(getContext(),3);
    private int mTabWidth;
    private int mCurrentPosition;
    private int mScreenWidth;

    private enum Model{
        TEXT_ONLY,//只显示文字
        DEFAULT//默认图标和文字
    }

    public MenuView(Context context) {
        super(context);
        initView(context);
    }
    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }
    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化容器视图
     * @param context
     */
    private void initView(Context context){
        setHorizontalScrollBarEnabled(false);//不要滚动条
        //设置最外层宽度自适应内容
        mParentContainer = new RelativeLayout(context);
        LayoutParams parentParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mParentContainer.setLayoutParams(parentParams);

        //设置tab容器宽度自适应内容
        mItemContainer = new LinearLayout(context);
        RelativeLayout.LayoutParams itemContainerParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        mItemContainer.setLayoutParams(itemContainerParams);

        mParentContainer.addView(mItemContainer);

        addView(mParentContainer);



    }

    /**
     * 初始化tab
     */
    private void initItems(int tabWidth){
        for (TabItem item : mItemDatas){
            initItem(tabWidth,item.getTitle(),item.getIcon());

        }
    }

    /**
     * 初始化单个tab视图
     * @param title
     * @param icon
     */
    private void initItem(int width,String title, String icon){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        final TabItemView itemView = new TabItemView(getContext());
        itemView.setTitleView(title);
        itemView.setLayoutParams(params);

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mItemContainer.indexOfChild(itemView);//获取是第几个子元素

                if (mTabItemClickListenr != null){
                    mTabItemClickListenr.onItemClick(itemView,mItemContainer,position);
                }


                int difX =(position-mCurrentPosition)*mTabWidth;
                moveBar(difX,Math.abs(position-mCurrentPosition)*200);
                mCurrentPosition = position;
                Log.i(TAG,"x --" + itemView.getX());

                scrollInScreen(itemView);

            }
        });

        mItemContainer.addView(itemView);
    }

    /**
     * 如果tab不完全在屏幕内，则移动HorizontalScrollView,使其完全展示在屏幕内
     *
     * @param itemView
     */
    private void scrollInScreen(View itemView){
        int[] location = new int[2];
        itemView.getLocationOnScreen(location);//获取在屏幕中的绝对位置

        int left = location[0];
        int change = 0;
        if (left<0){//如果点击项超出左边界
            change = left - mTabWidth;
        } else if (left+mTabWidth > mScreenWidth){//如果超出有屏幕
            change = left+mTabWidth-mScreenWidth + mTabWidth;
        }

        if (change!=0){
            smoothScrollBy(change,0);
        }
    }

    private void moveBar(int difX,int duration){
        Log.i(TAG,"left dif -- " + mBarView.getX());
        ObjectAnimator transAnim = ObjectAnimator.ofFloat(mBarView, "translationX", mBarView.getX(), mBarView.getX()+difX);

        transAnim.setDuration(DEFAULT_DURATION);
        transAnim.start();;
    }

    /**
     * 通知刷新界面
     */
    public void notifyDataChange(){
        int visibleCount = calVisibleCount();
        mTabWidth = calTabWidth(visibleCount);
        initItems(mTabWidth);
        addBarView(isShowBar,mTabWidth);
    }

    /**
     * 计算可见tab数量
     * 如果数据量小于DEFAULT_VISIBLE_COUNT，则数据全部可见
     * 如果 用户设置的可见数量大于tab的可用数量 那么tab全部可见
     *
     * @return int 可见数量
     */
    private int calVisibleCount(){
        int size = mItemDatas.size();
        int visibleCount = size < DEFAULT_VISIBLE_COUNT ? size : DEFAULT_VISIBLE_COUNT;
        visibleCount = mVisibleCount>mItemDatas.size()?visibleCount : mVisibleCount;
        return visibleCount;
    }

    /**
     * 根据tab数量计算 tab width
     *
     * @return int tab width
     */
    private int calTabWidth(int visibleCount){
        mScreenWidth = getScreenWidth(getContext());

        int tabWidth = (mScreenWidth-mDefaultPaddingLR*2)/visibleCount;
        return tabWidth;
    }

    private void addBarView(boolean isShowBar,int tabWidth){
        if (isShowBar){
            RelativeLayout.LayoutParams barViewParams = new RelativeLayout.LayoutParams(tabWidth,mBarHeight);
            barViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBarView = new ImageView(getContext());
            mBarView.setImageResource(R.color.colorPrimary);
            mBarView.setLayoutParams(barViewParams);
            mParentContainer.addView(mBarView);
        }
    }

    /**
     * 设置tab点击监听
     *
     * @param tabItemClickListenr <see>ITabItemClickListenr</see>
     */
    public void setOnTabItemClickListenr(OnTabItemClickListener tabItemClickListenr) {
        this.mTabItemClickListenr = tabItemClickListenr;
    }

    /**
     * 设置tab 可见数量
     *
     * @param count
     */
    public void setVisibleCount(int count){
        this.mVisibleCount = count;
    }



    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return int 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }

    /**
     * 获取屏幕的高度
     *
     * @param context
     * @return int 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getHeight();
    }

    public void setmItemDatas(List<TabItem> mItemDatas) {
        this.mItemDatas = mItemDatas;
    }

    public void setmDefaultPaddingTB(int mDefaultPaddingTB) {
        this.mDefaultPaddingTB = mDefaultPaddingTB;
    }

    public void setmDefaultPaddingLR(int mDefaultPaddingLR) {
        this.mDefaultPaddingLR = mDefaultPaddingLR;
    }



    /**
     * Tab 项点击监听
     */
    public interface OnTabItemClickListener{
        /**
         * 点击tab 响应事件
         *
         * @param tabView
         * @param parentView
         * @param position tab 索引位置
         */
        public void onItemClick(View tabView, View parentView,int position);
    }
}
