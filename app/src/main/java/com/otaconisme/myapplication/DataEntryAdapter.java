package com.otaconisme.myapplication;


import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
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
    public View getView(final int i, View inputView, ViewGroup viewGroup) {
        View view = inputView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.data_entry, null);//TODO replace null with something else
        }

        TextView listItemText = (TextView) view.findViewById(data_entry_list);
        if (listItemText != null) {
            listItemText.setText((i + 1) + ". " + list.get(i).toString());
        }

        Button deleteBtn = (Button) view.findViewById(R.id.data_entry_delete_btn);

        if (deleteBtn != null) {
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(i);
                    notifyDataSetChanged();
                    //Util.generateBarChartSpeed();

                }
            });
        }

        final ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.my_switcher);
        //switcher.showNext(); //or switcher.showPrevious();
        TextView textView = (TextView) switcher.findViewById(R.id.data_entry_list);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switcher.showNext();
                v.requestFocus();
            }
        });

        EditText editText = (EditText) switcher.findViewById(R.id.data_entry_list_edit);
//        editText.setOnClickListener((new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //switcher.reset();
//                switcher.showNext();
//                DataEntry dataEntry = DataListFragment.dataList.get(i);
//                if (editText.getText() != null) {
//                    double speed = Double.parseDouble(editText.getText().toString());
//                    long time = Double.valueOf(100 / speed).longValue();
//                    dataEntry.setTime(time);
//                    dataEntry.updateSpeed();
//                    textView.setText(dataEntry.toString());
//                }
//                v.requestFocus();
//            }
//        }));

//        editText.setOnFocusChangeListener((new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    switcher.showNext();
//                }
//            }
//        }));

        return view;
    }
}
