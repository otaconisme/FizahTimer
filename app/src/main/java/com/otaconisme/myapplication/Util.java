package com.otaconisme.myapplication;

import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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

        double lowerLimit = Math.floor(min / 10) * 10;
        double upperLimit = Math.ceil(max / 10) * 10;


        double spaces = 5;
        if (inputData.size() < 5) {
            spaces = inputData.size();
        }
        if (spaces <= 0) {
            spaces = 1;
        }
        double spaceSize = (upperLimit - lowerLimit) / spaces;//TODO figure this things out

        HashMap<Double, Double> table = new HashMap<>();

        for (double speed : inputData) {
            for (double limit = lowerLimit; limit <= upperLimit; limit += spaceSize) {
//                if (table.get(limit) == null) {
//                    table.put(limit, 0.0);
//                }
//                if (table.get(limit) != null) {
//                    if (speed < limit) {
//                        double count = table.get(limit);
//                        table.put(limit, ++count);
//                    }
//                }
                if (table.get(limit) == null) {
                    table.put(limit, 0.0);
                }

                if (speed >= limit && speed < (limit + spaceSize)) {
                    table.put(limit, table.get(limit) + 1);
                }
            }
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<>();

        for (int i = 0; i < spaces; i++) {
            double limit = lowerLimit + i * spaceSize;
            float yvalue = (float) (double) table.get(limit);
            yVals1.add(new BarEntry(i+1, yvalue));
        }


        //mChart = (BarChart) findViewById(R.id.chart1);
        if (barChart == null) {
            barChart = (BarChart) barChartView;

            barChart.setMaxVisibleValueCount(60);

            barChart.setPinchZoom(false);

            barChart.setDrawBarShadow(false);
            barChart.setDrawGridBackground(false);


            XAxis xAxis = barChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);

            YAxis yAxisLeft = barChart.getAxisLeft();
            yAxisLeft.setGranularityEnabled(true);
            yAxisLeft.setGranularity(1.000f);

            YAxis yAxisRight = barChart.getAxisRight();
            yAxisRight.setGranularityEnabled(true);
            yAxisRight.setGranularity(1.000f);
            // add a nice and smooth animation

            barChart.getLegend().setEnabled(false);

            barChart.getAxisLeft().setDrawGridLines(false);

        }

        barChart.animateY(2500);
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
