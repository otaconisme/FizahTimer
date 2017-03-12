package com.otaconisme.myapplication;

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
}
