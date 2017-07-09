package com.otaconisme.myapplication;


import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
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

        final ViewSwitcher switcher = (ViewSwitcher) view.findViewById(R.id.my_switcher);
        final TextView itemText = (TextView) view.findViewById(data_entry_list);
        final EditText editText = (EditText) view.findViewById(R.id.data_entry_list_edit);
        Button deleteBtn = (Button) view.findViewById(R.id.data_entry_delete_btn);
        TextView itemNumber = (TextView) view.findViewById(R.id.data_entry_list_number);

        if (itemNumber != null) {
            //TODO change string concatenation to string with placeholders
            itemNumber.setText((i + 1) + ". ");
        }

        if (itemText != null) {
            itemText.setText(list.get(i).toString());
        }

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

        itemText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO fix null pointer exception possibility
                editText.setText(Double.toString(list.get(i).getSpeedKMH()));
                switcher.showNext();
                v.requestFocus();
            }
        });

//        editText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                listItemText.setText(editText.getText());
//                //switcher.showNext();
//                //v.requestFocus();
//            }
//        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus){
                if(!hasFocus){
                    try {
                        double input = Double.parseDouble(editText.getText().toString());
                        if (!Double.isNaN(input)) {
                            list.get(i).setSpeedKMH(input);
                            //TODO does this need to be called in another thread?
                            notifyDataSetChanged();
                        }
                    }catch (Exception e){
                        //TODO do something
                    }finally {
                        switcher.showNext();
                    }
                }
            }

        });

        return view;
    }
}
