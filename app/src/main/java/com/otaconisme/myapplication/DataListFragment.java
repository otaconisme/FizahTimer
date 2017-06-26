package com.otaconisme.myapplication;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Zakwan on 3/12/2017.
 */

public class DataListFragment extends ListFragment {

    public static ArrayList<DataEntry> dataList;
    public static DataEntryAdapter adapter;//TODO fix how to do adapter

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        dataList = new ArrayList<>();
        adapter = new DataEntryAdapter(dataList, getActivity());
        setListAdapter(adapter);
    }
}
