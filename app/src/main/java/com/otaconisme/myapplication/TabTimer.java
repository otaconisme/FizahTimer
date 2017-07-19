package com.otaconisme.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.ViewSwitcher;

/**
 * Created by Zakwan on 3/1/2017.
 */

public class TabTimer extends Fragment {
    
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_timer, container, false);

        final ViewSwitcher inputViewSwitcher = (ViewSwitcher) rootView.findViewById(R.id.view_switcher_input);
        //Switch between timer and manual input
        final Switch inputSwitch =(Switch) rootView.findViewById(R.id.switch_input);
        inputSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if(inputSwitch.isChecked()){
                    inputViewSwitcher.showNext();
                }else{
                    inputViewSwitcher.reset();
                    inputViewSwitcher.showNext();
                }

            }
        });

        return rootView;
    }
}
