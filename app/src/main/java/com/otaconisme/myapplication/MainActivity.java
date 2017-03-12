package com.otaconisme.myapplication;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.R.attr.country;
import static android.R.attr.data;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    Handler mHandler = new Handler();
    long startTime = 0, totalTime = 0;
    int REFRESH_RATE = 61;
    ArrayList<DataEntry> dataList;
    MyListAdapter dataAdapter = null;


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroy thread;
        mHandler.removeCallbacks(startTimer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());//cache tab to the tab limit

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //data list
        ListView listView = (ListView) findViewById(R.id.listView01);
        dataList = new ArrayList<DataEntry>();
        dataAdapter = new MyListAdapter(this, R.layout.tab_list, dataList);

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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position) {
                case 0:
                    TabTimer tabTImer = new TabTimer();
                    return tabTImer;
                case 1:
                    TabList tabList = new TabList();
                    return tabList;
                case 2:
                    TabReport tabReport = new TabReport();
                    return tabReport;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Timer";
                case 1:
                    return "List";
                case 2:
                    return "Report";
            }
            return null;
        }
    }

    public void startTimer(View view){
        Button button = (Button) findViewById(R.id.button_start_stop_timer);
        if(button.getText().equals("Start")) {
            button.setText(getString(R.string.button_stop_timer_text));
            startTime = System.currentTimeMillis();
            mHandler.removeCallbacks(startTimer);
            mHandler.postDelayed(startTimer, 0);
        }else{
            button.setText(getString(R.string.button_start_timer_text));
            mHandler.removeCallbacks(startTimer);
            dataList.add(new DataEntry(totalTime));
            //updateTimer(totalTime);
        }
    }


    public void updateTimer(long totalTime){
        //TODO update this to proper
        TextView timer = (TextView) findViewById(R.id.timer_display);
        String mili, second, minute;
        int ms,s,m;

        m = (int) totalTime/60000;
        if(m<10) {
            minute = "  " + m;
        }else if(m<100){
            minute = " " + m;
        }else{
            minute = "" + m;
        }

        s = (int) (totalTime - m*60000)/1000 ;
        if(s<10){
            second = "0"+s;
        }else {
            second = "" + s;
        }

        ms = (int) totalTime%1000;
        if(ms>99){
            mili = "" + ms;
        }else if(ms>9){
            mili = "0" + ms;
        }else {
            mili = "00" + ms;
        }

        timer.setText(minute + ":" +second +"." + mili);
    }


    private Runnable startTimer = new Runnable() {
        public void run() {
            totalTime = System.currentTimeMillis() - startTime;
            updateTimer(totalTime);
            mHandler.postDelayed(this,REFRESH_RATE);
        }
    };

    private class MyListAdapter extends ArrayAdapter<DataEntry>{

        private ArrayList<DataEntry> dataList;

        public MyListAdapter(Context context, int textViewResourceId, ArrayList<DataEntry> dataList){
            super(context, textViewResourceId, dataList);
            this.dataList = new ArrayList<DataEntry>();
            this.dataList.addAll(dataList);
        }

        private class ViewHolder {
            TextView timer;
        }

        public void add(DataEntry dataEntry){
            Log.v("AddView", ""+dataEntry.getTime());
            this.dataList.add(dataEntry);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));
            if(convertView == null){
                LayoutInflater vi = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.tab_list, null);

                holder = new ViewHolder();
                holder.timer = (TextView) convertView.findViewById(R.id.time);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            DataEntry dataEntry = this.dataList.get(position);
            holder.timer.setText(""+dataEntry.getTime());

            return convertView;
        }
    }
}