package com.vicky.firstproject.view;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IntRange;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.vicky.firstproject.R;
import com.vicky.firstproject.util.DensityUtil;

import java.util.List;

/**
 * Created by lenovo on 2016/6/17.
 */
public class MenuView extends HorizontalScrollView {
    private final String TAG = "MenuView";

    private final int DEFAULT_VISIBLE_COUNT = 3;//默认可见tab数量
    private final int DEFAULT_DURATION = 300;//默认滑块动画时间
    private final int DEFAULT_BAR_COLOR = 0XCD5555;//默认滑块颜色

    private final boolean DEFAULT_IS_BAR_SHOW = true;

    private final int DEFAULT_BAR_HEIGHT = DensityUtil.dip2px(getContext(),3);//默认滑块高度

    private RelativeLayout mParentContainer;//最外层容器
    private LinearLayout mItemContainer;//tab 容器
    private ImageView mBarView;//下滑条视图
    private boolean isShowBar = DEFAULT_IS_BAR_SHOW;//是否显示滑块
    private int mDuration = DEFAULT_DURATION;

    private OnTabItemClickListener mTabItemClickListenr;

    private int mDefaultPaddingTB = DensityUtil.dip2px(getContext(),3);
    private int mDefaultPaddingLR = DensityUtil.dip2px(getContext(),3);


    private int mTabWidth;
    private int mCurrentPosition;
    private int mScreenWidth;

    private int mVisibleCount;
    private int mBarHeight = DEFAULT_BAR_HEIGHT;
    private int mBarColor;

    private BaseAdapter mAdapter;

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

        mBarColor = a.getColor(R.styleable.custom_menu_view_barColor,DEFAULT_BAR_COLOR);
        mVisibleCount = a.getInt(R.styleable.custom_menu_view_visibleCount,DEFAULT_VISIBLE_COUNT);
        mVisibleCount = mVisibleCount>0 ? mVisibleCount:DEFAULT_VISIBLE_COUNT;
        isShowBar = a.getBoolean(R.styleable.custom_menu_view_isBarShow,DEFAULT_IS_BAR_SHOW);
        mBarHeight = a.getDimensionPixelOffset(R.styleable.custom_menu_view_barHeight,DEFAULT_BAR_HEIGHT);
        mDuration = a.getInt(R.styleable.custom_menu_view_duration,DEFAULT_DURATION);
        a.recycle();
    }

    private void buildMenuItems(int tabWidth){
        for (int i = 0, size = mAdapter.getCount(); i < size;i++){
            final View view = mAdapter.getView(i,null,this);
            View itemView = buildItem(view, tabWidth,i);
            mItemContainer.addView(itemView);
        }
    }

    /**
     * 初始化单个tab视图
     */
    private View buildItem(final View view, final int tabWidth, final int position){

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(tabWidth, ViewGroup.LayoutParams.MATCH_PARENT);

        view.setLayoutParams(params);
        view.setPadding(mDefaultPaddingLR,mDefaultPaddingTB,mDefaultPaddingLR,mDefaultPaddingTB);

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentPosition==0&&position==0||mCurrentPosition!= position){
                    View lastView = mItemContainer.getChildAt(mCurrentPosition);
                    if (mTabItemClickListenr != null){
                        mTabItemClickListenr.onItemClick(mItemContainer,view,lastView,position,mCurrentPosition);
                    }
                    scrollInScreen(view);

                    int difX =(position-mCurrentPosition)*tabWidth;
                    if (isShowBar){
                        moveBar(difX,mDuration);
                    }
                    mCurrentPosition = position;
                }
            }
        });

        return view;
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

        transAnim.setDuration(duration<=0?DEFAULT_DURATION:duration);
        transAnim.start();
    }

    /**
     * 通知刷新界面
     */
    public void notifyDataChange() {
        if (mAdapter==null){
            Log.e(TAG,"你没有设置adapter");
            return;
        }

        if (mAdapter.getCount()==0){
            Log.i(TAG,"没有数据");
            return;
        }
        int visibleCount = calVisibleCount();
        mTabWidth = calTabWidth(visibleCount);
        buildMenuItems(mTabWidth);
        addBarView(isShowBar,mTabWidth);
        mItemContainer.getChildAt(0).performClick();
    }

    /**
     * 计算可见tab数量
     * 如果数据量小于DEFAULT_VISIBLE_COUNT，则数据全部可见
     * 如果 用户设置的可见数量大于tab的可用数量 那么tab全部可见
     *
     * @return int 可见数量
     */
    private int calVisibleCount(){
        int size = mAdapter.getCount();
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
    public void setVisibleCount(@IntRange(from = 1) int count){
        this.mVisibleCount = count;
        this.mVisibleCount = this.mVisibleCount>0 ? this.mVisibleCount : DEFAULT_VISIBLE_COUNT;
    }

    /**
     * 设置某个item选中
     * @param position
     */
    public void setItemSelected(@IntRange(from = 0) int position){
        if (mAdapter!= null && mAdapter.getCount()>0 && mAdapter.getCount()>position&&
                mCurrentPosition != position && mItemContainer.getChildAt(position)!= null){
            mItemContainer.getChildAt(position).performClick();
        }
    }

    /**
     * 设置滑块动画时间
     * @param duration
     */
    public void setDuration(@IntRange(from = 0) int duration){
        this.mDuration = duration;
    }


    /**
     * 获取屏幕的宽度
     *
     * @param context
     * @return int 屏幕宽度
     */
    private int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }



    public void setmDefaultPaddingTB(int mDefaultPaddingTB) {
        this.mDefaultPaddingTB = mDefaultPaddingTB;
    }

    public void setmDefaultPaddingLR(int mDefaultPaddingLR) {
        this.mDefaultPaddingLR = mDefaultPaddingLR;
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    public void setAdapter(BaseAdapter adapter){
        this.mAdapter = adapter;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        notifyDataChange();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    /**
     * Tab 项点击监听
     */
    public interface OnTabItemClickListener{

        /**
         * tab 点击事件
         *
         * @param parentView 父view
         * @param currentTab 当前点击的tab
         * @param lastTab 上次点击的tab
         * @param currentPos 当前点击的位置
         * @param lastPos 上次点击的位置
         */
        void onItemClick(View parentView,View currentTab, View lastTab,int currentPos,int lastPos);
    }

    public static abstract class BaseMenuAdapter<T> extends BaseAdapter{
        private List<T> mItems;

        public BaseMenuAdapter(List<T> items){
            this.mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public T getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(getLayoutId(),parent,false);
            bindData(convertView,position);
            return convertView;
        }

        /**
         * 设置item的布局
         *
         * @return
         */
        public abstract int getLayoutId();

        /**
         * 绑定数据
         * @param itemView
         * @param position
         */
        public abstract void bindData(View itemView, int position);

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();

        }
    }
}
