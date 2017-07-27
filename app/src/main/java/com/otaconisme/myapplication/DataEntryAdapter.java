package com.otaconisme.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

import static com.otaconisme.myapplication.R.id.data_entry_list;

/**
 * Created by Zakwan on 6/18/2017.
 */

public class DataEntryAdapter extends BaseAdapter implements ListAdapter {

    private ArrayList<DataEntry> list = new ArrayList<>();
    private Context context;
    private int selectedIndex = -1;

    public DataEntryAdapter(ArrayList<DataEntry> list, Context context) {
        this.list = list;
        this.context = context;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final ViewGroup nullParent = null;
            view = inflater.inflate(R.layout.data_entry, nullParent);
        }

        final ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.data_entry_switcher);
        final TextView itemText = (TextView) view.findViewById(data_entry_list);
        final EditText editText = (EditText) view.findViewById(R.id.data_entry_list_edit);
        Button deleteBtn = (Button) view.findViewById(R.id.data_entry_delete_btn);
        TextView itemNumber = (TextView) view.findViewById(R.id.data_entry_list_number);

        if (itemNumber != null) {
            String number = (i + 1) + ". ";
            itemNumber.setText(number);
        }

        if (itemText != null) {
            itemText.setText(list.get(i).toString());
        }

        if (deleteBtn != null) {
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(i);
                    switcher.reset();
                    switcher.showNext();
                    v.clearFocus();
                    ((MainActivity)context).notifyDataListChanged();
                }
            });
        }

        //check if view is selected
        if(selectedIndex!=i){
            switcher.setDisplayedChild(0);
        }else{
            switcher.setDisplayedChild(1);
            //fix for edittext become empty after scrolling for a while
            String speed = String.valueOf(list.get(i).getSpeedKMH());
            editText.setText(speed);
        }

        if(itemText!=null) {
            itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.setText(String.valueOf(list.get(i).getSpeedKMH()));
                    selectedIndex = i;
                    //reset other view to not selected
                    ListView lv = getParentListView(v.getParent());
                    for (int j = 0; j < lv.getChildCount(); j++) {
                        ViewSwitcher vs = (ViewSwitcher) lv.getChildAt(j).findViewById(R.id.data_entry_switcher);
                        vs.setDisplayedChild(0);
                    }
                    switcher.showNext();
                    v.requestFocus();
                }
            });
        }

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        double input = Double.parseDouble(editText.getText().toString());
                        if (!Double.isNaN(input)) {
                            list.get(i).setSpeedKMH(input);
                            ((MainActivity)context).notifyDataListChanged();
                        }
                    } catch (Exception e) {
                        //TODO do something
                    } finally {
                        selectedIndex=-1;//reset
                        switcher.showNext();
                    }
                }
            }

        });

        return view;
    }

    private ListView getParentListView(ViewParent vp){
        if(vp instanceof  ListView){
            return (ListView) vp;
        }else{
            return getParentListView(vp.getParent());
        }
    }
}
