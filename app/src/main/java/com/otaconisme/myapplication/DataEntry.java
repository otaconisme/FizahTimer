package com.otaconisme.myapplication;

/**
 * Created by Zakwan on 3/9/2017.
 */

public class DataEntry {
    private long time;

    DataEntry(long time){
        setTime(time);
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }

    @Override
    public String toString(){
        return Util.transformTime(getTime());
    }
}
