package com.otaconisme.myapplication;

import java.util.ArrayList;

import static android.R.attr.max;

/**
 * Created by Zakwan on 3/12/2017.
 */

public class Util {

    public static String transformTime(long totalTime){

        String mili, second, minute;
        int ms,s,m;

        m = (int) totalTime/60000;
        if(m<10) {
            minute = "  " + m;
        }else if(m<100){
            minute = " " + m;
        }else{
            minute = "" + m;
        }

        s = (int) (totalTime - m*60000)/1000 ;
        if(s<10){
            second = "0"+s;
        }else {
            second = "" + s;
        }

        ms = (int) totalTime%1000;
        if(ms>99){
            mili = "" + ms;
        }else if(ms>9){
            mili = "0" + ms;
        }else {
            mili = "00" + ms;
        }
        return minute + ":" +second +"." + mili;
    }

    public static double getMax(ArrayList<Double> input){
        double max = 0.0;
        for(double in: input){
            if(in>max){
                max = in;
            }
        }
        return max;
    }

    public static double getMin(ArrayList<Double> input){
        double min = 0.0;
        for(double in: input){
            if(min==0.0){
                min = in;
            }
            if(in<min){
                min = in;
            }
        }
        return min;
    }

    public static double getAverage(ArrayList<Double> input){
        double result = 0.0;
        if(input.size()>0) {
            for (double in : input) {
                result += in;
            }
            result /= input.size();
        }
        return result;
    }

    public static double getVariance(ArrayList<Double> input, Double average) {
        double result = 0.0;
        if (input.size() > 0){
            for (double in : input) {
                result += Math.abs(in - average);
            }
            result /= input.size();
        }
        return result;
    }
}
