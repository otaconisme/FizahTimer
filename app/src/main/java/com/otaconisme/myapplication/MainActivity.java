package com.otaconisme.myapplication;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private DataEntryAdapter dataEntryAdapter = null;
    private ArrayList<DataEntry> dataList;
    //Constant
    final int REFRESH_RATE = 61;
    final int TOTAL_TAB_COUNT = 2;
    final double distanceInputDefault = 100.00;
    //var
    long startTime = 0, totalTime = 0;
    double gDistanceInput = distanceInputDefault;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //destroy thread;
        mHandler.removeCallbacks(startTimer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dataList = new ArrayList<>();
        dataEntryAdapter = new DataEntryAdapter(dataList, this);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(mSectionsPagerAdapter.getCount());//cache tab to the tab limit

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
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
    private class SectionsPagerAdapter extends FragmentPagerAdapter {

        private SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);

            switch (position) {
                case 0:
                    return new TabTimer();
                case 1:
                    return new TabReport();
            }
            return null;
        }

        @Override
        public int getCount() {
            return TOTAL_TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Input";
                case 1:
                    return "Report";
            }
            return null;
        }
    }

    //TODO: update speed when distance is changed
    private boolean isDistanceChanged() {
//        EditText et = (EditText) findViewById(R.id.distance);
//        double currentDistance = Double.parseDouble(et.getText().toString());
//        if(currentDistance==gDistanceInput){
//            return false;
//        }
        return true;
    }

    public void startTimer(View view) {
        Switch inputSwitch = (Switch) findViewById(R.id.switch_input);

        if(!inputSwitch.isChecked()) {


            if (getMainButtonText().equals("Start")) {
                setMainButtonText(getString(R.string.button_stop_timer_text));
                startTime = System.currentTimeMillis();
                mHandler.removeCallbacks(startTimer);
                mHandler.postDelayed(startTimer, 0);
            } else {
                setMainButtonText(getString(R.string.button_start_timer_text));
                mHandler.removeCallbacks(startTimer);
                //update to list
                dataList.add(new DataEntry(totalTime, gDistanceInput));

                notifyDataListChanged();
            }
        }else{

            EditText editText = (EditText) findViewById(R.id.edit_text_input);
            double input = Double.parseDouble(editText.getText().toString());
            editText.setText("");
            try {
                if (!Double.isNaN(input)) {
                    dataList.add(new DataEntry(input));

                    notifyDataListChanged();

                }
            }catch (NullPointerException ne){
                //do nothing
            }

        }
    }

    public void notifyDataListChanged(){

        runOnUiThread(new Runnable() {
            public void run() {
                dataEntryAdapter.notifyDataSetChanged();
                //TODO fix this bug
                if (dataList.size() > 4) {
                    generateReport();
                }
            }
        });

        showHideClearAll();
    }

    public void updateTimerDisplay(long totalTime) {
        TextView timer = (TextView) findViewById(R.id.timer_display);
        timer.setText(Util.transformTime(totalTime));
    }

    /**
     * Timer separate thread
     */
    private Runnable startTimer = new Runnable() {
        public void run() {
            //total time is set here and used by other funtion
            totalTime = System.currentTimeMillis() - startTime;
            updateTimerDisplay(totalTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };

    public void generateReport() {
        ArrayList<Double> input = new ArrayList<>();
        for (DataEntry de : dataList) {
            input.add(de.getSpeed() * 3600);
        }
        //TODO clean this
        TextView mean = (TextView) findViewById(R.id.mean_value);
        mean.setText("" + Util.getAverage(input));
        TextView min = (TextView) findViewById(R.id.min_value);
        min.setText("" + Util.getMin(input));
        TextView max = (TextView) findViewById(R.id.max_value);
        max.setText("" + Util.getMax(input));
        TextView var = (TextView) findViewById(R.id.variance_value);
        var.setText("" + Util.getVariance(input, Util.getAverage(input)));
        View barChartView = findViewById(R.id.chart1);
        if (barChartView != null) {
            Util.generateBarChart(input, barChartView);
        }
    }

    public DataEntryAdapter getDataEntryAdapter() {
        return dataEntryAdapter;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void setMainButtonText(String text){
        Button button = (Button) findViewById(R.id.button_start_stop_timer);
        button.setText(text);
    }

    public String getMainButtonText(){
        Button button = (Button) findViewById(R.id.button_start_stop_timer);
        return button.getText().toString();
    }

    public void clearAllDataEntry(View view){
        dataList.clear();
        notifyDataListChanged();
    }

    public void showHideClearAll(){
        ViewSwitcher viewSwitcher = (ViewSwitcher) findViewById(R.id.view_switcher_clear_data_button);

        if(dataList.size()>0){
            View view = viewSwitcher.getNextView();
            if(view instanceof Button){
                viewSwitcher.showNext();
            }
        }else{
            viewSwitcher.showNext();
        }
    }
}