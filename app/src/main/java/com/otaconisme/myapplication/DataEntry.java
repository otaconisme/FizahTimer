package com.otaconisme.myapplication;

import static android.R.attr.value;
import static com.otaconisme.myapplication.R.id.distance;
import static java.lang.Math.round;

/**
 * Created by Zakwan on 3/9/2017.
 */

public class DataEntry {
    private long time;
    private double speed;

    DataEntry(long time, double distance){
        setTime(time);
        updateSpeed(distance);
    }

    public long getTime(){
        return time;
    }

    public void setTime(long time){
        this.time = time;
    }

    public double getSpeed(){
        return speed;
    }

    private void setSpeed(double speed){
        this.speed = speed;
    }

    @Override
    public String toString(){
        //time in milli second
        return Util.transformTime(getTime()) + " s @" + String.format("%.2f", getSpeed()*3600) + " km/h";
    }

    public void updateSpeed(double distance){
        this.setSpeed(distance/this.getTime());
    }
}
