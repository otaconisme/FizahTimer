package com.otaconisme.myapplication;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * Created by Zakwan on 3/12/2017.
 */

public class DataListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataEntryAdapter adapter = ((MainActivity) getActivity()).getDataEntryAdapter();
        getListView().setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        setListAdapter(adapter);
    }
}
