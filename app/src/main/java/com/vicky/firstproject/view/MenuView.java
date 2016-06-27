package com.vicky.firstproject.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
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

    private final int DEFAULT_VISIBLE_COUNT = 3;//默认可见tab数量
    private final int DEFAULT_DURATION = 300;//默认滑块动画时间
    private final int DEFAULT_TEXT_COLOR = 0XB7B7B7 ;//默认文字颜色
    private final int DEFAULT_BAR_COLOR = 0XCD5555;//默认滑块颜色

    private final int ORIENTATION_VERTICAL = 0;
    private final int ORIENTATION_HORIZONTAL = 1;

    private final int  DEFAULT_ICON_ORIENTAION = ORIENTATION_VERTICAL;//默认图片文字布局
    private final boolean DEFAULT_IS_BAR_SHOW = true;


    private final int DEFAULT_TEXT_SIZE = DensityUtil.dip2px(getContext(),16);//默认文字大小
    private final int DEFAULT_BAR_HEIGHT = DensityUtil.dip2px(getContext(),3);//默认滑块高度

    private List<TabItem> mItemDatas = new ArrayList<>();
    private RelativeLayout mParentContainer;//最外层容器
    private LinearLayout mItemContainer;//tab 容器
    private ImageView mBarView;//下滑条视图
    private boolean isShowBar = DEFAULT_IS_BAR_SHOW;//是否显示滑块


    private OnTabItemClickListener mTabItemClickListenr;

    private int mDefaultPaddingTB = DensityUtil.dip2px(getContext(),3);
    private int mDefaultPaddingLR = DensityUtil.dip2px(getContext(),3);


    private int mTabWidth;
    private int mCurrentPosition;
    private int mScreenWidth;

    private int mTextColor = DEFAULT_TEXT_COLOR;
    private int mTextSelectedColor = DEFAULT_TEXT_COLOR;
    private int mTextSize;
    private int mVisibleCount;
    private int mBarHeight = DEFAULT_BAR_HEIGHT;
    private int mIconOrientation;
    private int mBarColor;




    private enum Model{
        TEXT_ONLY,//只显示文字
        DEFAULT//默认图标和文字
    }



    public MenuView(Context context) {
        super(context);
        initView(context,null);
    }
    public MenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }
    public MenuView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    /**
     * 初始化容器视图
     * @param context
     */
    private void initView(Context context,AttributeSet attrs){
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

        initStyle(attrs);

    }

    private void initStyle(AttributeSet attrs){
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.custom_menu_view);
        mTextColor = a.getColor(R.styleable.custom_menu_view_textColor,DEFAULT_TEXT_COLOR);
        mTextSelectedColor = a.getColor(R.styleable.custom_menu_view_textSelectedColor,DEFAULT_TEXT_COLOR);

        mTextSize = a.getDimensionPixelSize(R.styleable.custom_menu_view_textSize,DEFAULT_TEXT_SIZE);
        mBarColor = a.getColor(R.styleable.custom_menu_view_barColor,DEFAULT_BAR_COLOR);
        mVisibleCount = a.getInt(R.styleable.custom_menu_view_visibleCount,DEFAULT_VISIBLE_COUNT);
        mIconOrientation = a.getInt(R.styleable.custom_menu_view_iconOrientation,DEFAULT_ICON_ORIENTAION);
        isShowBar = a.getBoolean(R.styleable.custom_menu_view_isBarShow,DEFAULT_IS_BAR_SHOW);
        mBarHeight = a.getDimensionPixelOffset(R.styleable.custom_menu_view_barHeight,DEFAULT_BAR_HEIGHT);
        Log.i(TAG,"style --  " + mTextColor +"  " + mTextSize +"  " + mBarColor +"  " + mIconOrientation);
        a.recycle();
    }

    /**
     * 初始化tab
     */
    private void initItems(int tabWidth){
        for (TabItem item : mItemDatas){
            initItem(tabWidth,item.getTitle(),item.getIcon());
        }

        ((TabItemView)mItemContainer.getChildAt(0)).setTextSelected(true);
    }

    /**
     * 初始化单个tab视图
     * @param title
     * @param icon
     */
    private void initItem(int width, final String title, String icon){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT);
        final TabItemView itemView = new TabItemView(getContext());
        itemView.setTitle(title);
        itemView.setLayoutParams(params);
        itemView.setTextColor(mTextColor);
        itemView.setTextSelectColor(mTextSelectedColor);
        itemView.setImgBmp(BitmapFactory.decodeResource(getResources(),R.drawable.md_refresh_loading01));

        itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mItemContainer.indexOfChild(itemView);//获取是第几个子元素
                if (mCurrentPosition!= position){
                    TabItemView lastItem = (TabItemView) mItemContainer.getChildAt(mCurrentPosition);
                    lastItem.setTextSelected(false);//将上一个item设置为未选中状态
                    itemView.setTextSelected(true);//将当前item设置为选中状态

                    if (mTabItemClickListenr != null){
                        mTabItemClickListenr.onItemClick(itemView,mItemContainer,position);
                    }
                    scrollInScreen(itemView);

                    int difX =(position-mCurrentPosition)*mTabWidth;
                    moveBar(difX,Math.abs(position-mCurrentPosition)*200);
                    mCurrentPosition = position;
                }
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
        Log.i(TAG,"left --  " + left);

        int change = 0;
        if (left<mTabWidth){//如果点击项超出左边界
            change = left - mTabWidth;
        } else if (left+mTabWidth >= mScreenWidth-mTabWidth){//如果超出有屏幕
            change = left+mTabWidth-mScreenWidth + mTabWidth;
        }

        if (change!=0){
            smoothScrollBy(change,0);
        }
    }

    private void moveBar(int difX,int duration){
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
        mVisibleCount = mVisibleCount>size?visibleCount : mVisibleCount;
        Log.i(TAG,"visible count -- "  +mVisibleCount );
        return mVisibleCount;
    }

    /**
     * 获取可视tab数量
     *
     * @return
     */
    private int getVisibleCount(){
        return mVisibleCount;
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
            Log.i(TAG,"width -- " + tabWidth +"  " + mBarHeight);

            RelativeLayout.LayoutParams barViewParams = new RelativeLayout.LayoutParams(tabWidth,mBarHeight);
            barViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            mBarView = new ImageView(getContext());
            mBarView.setBackgroundColor(mBarColor);
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
