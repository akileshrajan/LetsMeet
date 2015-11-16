package com.utase1.letsmeet.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;
import android.widget.PopupMenu;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.utase1.letsmeet.activity.*;
import com.utase1.letsmeet.app.AppConfig;
import com.utase1.letsmeet.app.AppController;
import com.utase1.letsmeet.helper.SQLiteHandler;
import com.utase1.letsmeet.helper.SessionManager;
import com.utase1.letsmeet.R;
import com.utase1.letsmeet.helper.ViewPagerAdapter;
import com.utase1.letsmeet.helper.SlidingTabLayout;


public class MySchedule extends ActionBarActivity implements PopupMenu.OnMenuItemClickListener {

    // Declaring Your View and Variables

    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Accepted","Pending", "My Meeting"};
    int Numboftabs =3;
    private SQLiteHandler db;
    private SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_myschedule);

        findViewById(R.id.mainButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MySchedule.this, view);
                popupMenu.setOnMenuItemClickListener(MySchedule.this);
                popupMenu.inflate(R.menu.mainmenu);
                popupMenu.show();
            }
        });



        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout

        tabs.setViewPager(pager);




    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_meeting:
                Intent i = new Intent(MySchedule.this,
                        CreateMeeting.class);
                startActivity(i);
                finish();
                return true;
            case R.id.create_event:
                Intent i1 = new Intent(MySchedule.this,
                        CreateEvent.class);
                startActivity(i1);
                finish();
                case R.id.logout:
                    db = new SQLiteHandler(getApplicationContext());

                    // session manager
                    session = new SessionManager(getApplicationContext());
                    session.setLogin(false);

                    db.deleteUsers();

                    // Launching the login activity
                    Intent intent = new Intent(MySchedule.this, LoginActivity.class);
                    startActivity(intent);
                    finish();





        }
        return true;
    }

}

