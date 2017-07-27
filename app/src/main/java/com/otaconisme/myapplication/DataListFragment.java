package com.otaconisme.myapplication;

import android.app.ListFragment;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Zakwan on 3/12/2017.
 */

public class DataListFragment extends ListFragment {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DataEntryAdapter adapter = ((MainActivity) getActivity()).getDataEntryAdapter();
        getListView().setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
        getListView().setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        setListAdapter(adapter);
    }
}
