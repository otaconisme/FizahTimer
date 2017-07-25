package com.otaconisme.myapplication;

import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Zakwan on 3/12/2017.
 */

public class Util {

    public static BarChart barChart;

    public static String transformTime(long totalTime) {

        String mili, second, minute;
        int ms, s, m;

        m = (int) totalTime / 60000;
        minute = String.valueOf(m);

        s = (int) (totalTime - m * 60000) / 1000;
        if (s < 10) {
            second = "0" + s;
        } else {
            second = "" + s;
        }

        ms = (int) totalTime % 1000;
        if (ms > 99) {
            mili = "" + ms;
        } else if (ms > 9) {
            mili = "0" + ms;
        } else {
            mili = "00" + ms;
        }
        return minute + ":" + second + "." + mili;
    }

    public static double getMax(ArrayList<Double> input) {
        double max = 0.0;
        for (double in : input) {
            if (in > max) {
                max = in;
            }
        }
        return max;
    }

    public static double getMin(ArrayList<Double> input) {
        double min = 0.0;
        for (double in : input) {
            if (min == 0.0) {
                min = in;
            }
            if (in < min) {
                min = in;
            }
        }
        return min;
    }

    public static double getAverage(ArrayList<Double> input) {
        double result = 0.0;
        if (input.size() > 0) {
            for (double in : input) {
                result += in;
            }
            result /= input.size();
        }
        return result;
    }

    public static double getVariance(ArrayList<Double> input, Double average) {
        double result = 0.0;
        if (input.size() > 0) {
            for (double in : input) {
                result += Math.abs(in - average);
            }
            result /= input.size();
        }
        return result;
    }

    public static void updateSpeedAll(ArrayList<DataEntry> dataEntryList, double distance) {
        for (DataEntry dataEntry : dataEntryList) {
            dataEntry.setDistance(distance);
            dataEntry.updateSpeed();
        }
    }

    //TODO fix how to get the barchar view
    public static void generateBarChartSpeed(ArrayList<DataEntry> dataList) {
        ArrayList<Double> inputData = new ArrayList<>();
        for (DataEntry de : dataList) {
            inputData.add(de.getSpeed());
        }
        //TODO fix
        if (inputData.size() > 1) {
            generateBarChart(inputData, barChart);
        }
    }

    public static void generateBarChart(ArrayList<Double> inputData, View barChartView) {

        double min = Util.getMin(inputData);
        double max = Util.getMax(inputData);

        double lowerLimit = Math.round(min / 100) * 100;
        double upperLimit = Math.round(max / 100) * 100;

        double spaces = 5;
        double spaceSize = (upperLimit - lowerLimit) / spaces;//TODO figure this things out

        HashMap<Double, Double> table = new HashMap<>();

        for (double speed : inputData) {
            for (double limit = lowerLimit; limit <= upperLimit; limit += spaceSize) {
                if (table.get(limit) == null) {
                    table.put(limit, 0.0);
                }
                if (table.get(limit) != null) {
                    if (speed < limit) {
                        double count = table.get(limit);
                        table.put(limit, ++count);
                    }
                }
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < spaces; i++) {
            double limit = lowerLimit + i * spaceSize;
            float yvalue = (float) (double) table.get(limit);
            yVals1.add(new BarEntry(i, yvalue));
        }


        //mChart = (BarChart) findViewById(R.id.chart1);
        if (barChart == null) {
            barChart = (BarChart) barChartView;

            barChart.setMaxVisibleValueCount(60);

            barChart.setPinchZoom(false);

            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        // add a nice and smooth animation
        barChart.animateY(2500);

        barChart.getLegend().setEnabled(false);

        barChart.getAxisLeft().setDrawGridLines(false);
        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(yVals1, "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(false);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
            barChart.setFitBars(true);
        }

        barChart.invalidate();
    }
}
