package com.vicky.firstproject;

import android.animation.Animator;
import android.animation.ObjectAnimator;
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
import android.widget.Toast;

import com.vicky.firstproject.model.TabItem;
import com.vicky.firstproject.view.MenuView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG ="first_project";
    private MenuView mMenuView;
    private TextView mTextView;

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


    }

    private void initMenuData(){
        List<TabItem> items = new ArrayList<>();
        for (int i = 0; i < 20; i++){
            TabItem menuItem = new TabItem();
            menuItem.setIcon("");
            menuItem.setTitle("tab-" + i);
            items.add(menuItem);
        }

        mMenuView.setmItemDatas(items);
        mMenuView.setVisibleCount(5);
        mMenuView.setOnTabItemClickListenr(new MenuView.OnTabItemClickListener() {
            @Override
            public void onItemClick(View tabView, View parentView, int position) {
                Toast.makeText(getApplicationContext(),"click - " + position,Toast.LENGTH_LONG).show();
            }
        });
        mMenuView.notifyDataChange();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
