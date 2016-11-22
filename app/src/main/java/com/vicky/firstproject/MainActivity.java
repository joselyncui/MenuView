package com.vicky.firstproject;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.vicky.firstproject.model.TabItem;
import com.vicky.firstproject.view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG ="first_project";
    private MenuView mMenuView;
    List<String> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        Log.i(TAG,getComponentName()+"  " + getComponentName().getClassName());
        mMenuView = (MenuView) findViewById(R.id.menu_view);
        initMenuData();

        mMenuView.setAdapter(new MenuView.BaseMenuAdapter<String>(mItems) {
            @Override
            public int getLayoutId() {
                return R.layout.tab_item;
            }

            @Override
            public void bindData(View itemView, int position) {
                String item = getItem(position);
                TextView textView = (TextView) itemView.findViewById(R.id.text);
                textView.setText(item);
            }
        });

        mMenuView.setOnTabItemClickListenr(new MenuView.OnTabItemClickListener(){
            @Override
            public void onItemClick(View parentView, View currentTab, View lastTab, int currentPos, int lastPos) {
                TextView lastTv = (TextView) lastTab.findViewById(R.id.text);
                lastTv.setTextColor(Color.parseColor("#000000"));

                TextView currentTv = (TextView) currentTab.findViewById(R.id.text);
                currentTv.setTextColor(Color.parseColor("#FF4081"));
            }
        });

        mMenuView.post(new Runnable() {
            @Override
            public void run() {
                mMenuView.setItemSelected(4);
            }
        });




    }

    private void initMenuData(){

        for (int i = 0, size=10;i < size;i++){
            mItems.add("tab-" + i);
        }
//        mItems.add("我是天才");
//        mItems.add("世界和平");
//        mItems.add("德玛西亚");
//        mItems.add("兽人永不为奴");
//        mItems.add("我走过山时，山不说话");
//        mItems.add("我走过海时，还不说话");
//        mItems.add("小毛驴滴滴答答");
//        mItems.add("倚天剑伴我走天涯");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds mItems to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
