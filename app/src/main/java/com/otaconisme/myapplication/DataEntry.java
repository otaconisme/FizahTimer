package com.otaconisme.myapplication;

/**
 * Created by Zakwan on 3/9/2017.
 */

public class DataEntry {
    private long time;
    private double speed;
    private double distance;

    DataEntry(long time, double distance) {
        setTime(time);
        setDistance(distance);
        updateSpeed();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeedKMH() {
        return speed * 3600;
    }

    public void setSpeedKMH(double speed) {
        this.speed = speed/3600;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        if (distance >= 0.0) {
            this.distance = distance;
        } else {
            this.distance = 0.0;
        }
    }

    @Override
    public String toString() {
        //time in milli second
        //TODO fix the default locale bug potential
        return String.format("%.2f", getSpeed() * 3600) + " km/h";
    }

    public void updateSpeed() {
        this.setSpeed(this.distance / this.getTime());
    }
}
