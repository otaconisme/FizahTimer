package com.otaconisme.myapplication;

import android.os.Bundle;
import android.view.View;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static android.R.attr.cropToPadding;
import static android.R.attr.max;
import static android.R.attr.saveEnabled;

/**
 * Created by Zakwan on 3/12/2017.
 */

class Util {

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

    public static double getPercentile(ArrayList<Double> input, double percentile){
        ArrayList<Double> data = new ArrayList<>(input);
        if(input.size()>1) {
            Collections.sort(data);
            double target = percentile * input.size()-1;
            if (target % 1 == 0) {
                if((int)target>0) {
                    return data.get((int) target - 1);
                }
            }

            //approximate value using linear
            double x1 = data.get((int) target);
            double y1 = (int) target;
            double x2 = data.get((int) target+1);
            double y2 = (int) target+1;
            double gradient = (y2 - y1) / (x2 - x1);
            double offset = y1 - (gradient * x1);
            return (target - offset) / gradient;
        }
        return 0;
    }
    public static double getMax(ArrayList<Double> input) {
        double max = 0.0;
        for (double in : input) {
            if (in >= max) {
                max = in;
            }
        }
        return max;
    }

    public static double getMin(ArrayList<Double> input) {
        double min = input.get(0);
        for (double in : input) {
            if (in <= min) {
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

    private static HashMap<Integer, String> getXAxisLabel(ArrayList<Double> inputData) {
        HashMap<Integer, String> result = new HashMap<>();

        double lowerLimit = Math.floor(Util.getMin(inputData) / 10) * 10;
        double upperLimit = Math.ceil(Util.getMax(inputData) / 10) * 10;
        double spaceSize = 10;
        double spaces = (upperLimit-lowerLimit)/spaceSize;
        //fix missing value
        if(upperLimit==Util.getMax(inputData)){
            spaces++;
        }

        for (int i = 1; i <= spaces; i++) {
            int currentLowerLimit = (int) (lowerLimit + (i - 1) * spaceSize);
            int currentUpperLimit = (int) (lowerLimit + i * spaceSize);
            String label = "(" + currentLowerLimit + "-" + currentUpperLimit + ")";
            result.put(i, label);
        }

        result.put(0, " ");//for line graph to start at 0
        result.put((int) spaces + 1, " ");//for line graph to end with the last value

        return result;
    }

    private static ArrayList<BarEntry> generateFrequencyChartData(ArrayList<Double> inputData) {
        ArrayList<BarEntry> result = new ArrayList<>();
        HashMap<Double, Double> table = new HashMap<>();

        try {

            double lowerLimit = Math.floor(Util.getMin(inputData) / 10) * 10;
            double upperLimit = Math.ceil(Util.getMax(inputData) / 10) * 10;
            double spaceSize = 10;
            double spaces = (upperLimit-lowerLimit)/spaceSize;
            //fix missing value
            if(upperLimit==Util.getMax(inputData)){
                spaces++;
            }

            for (double speed : inputData) {
                for (double limit = lowerLimit; limit <= upperLimit; limit += spaceSize) {

                    if (table.get(limit) == null) {
                        table.put(limit, 0.0);
                    }

                    if (speed >= limit && speed < (limit + spaceSize)) {
                        table.put(limit, table.get(limit) + 1);
                    }
                }
            }

            for (int i = 0; i < spaces; i++) {
                double limit = lowerLimit + i * spaceSize;
                float yvalue = (float) (double) table.get(limit);
                result.add(new BarEntry(i + 1, yvalue));
            }
        } catch (Exception e) {
            //TODO
        }
        return result;
    }

    private static ArrayList<Entry> generateHistoChartData(ArrayList<Double> inputData) {
        ArrayList<Entry> result = new ArrayList<>();
        HashMap<Double, Double> table = new HashMap<>();

        try {

            double lowerLimit = Math.floor(Util.getMin(inputData) / 10) * 10;
            double upperLimit = Math.ceil(Util.getMax(inputData) / 10) * 10;
            double spaceSize = 10;
            double spaces = (upperLimit-lowerLimit)/spaceSize;
            //fix missing value
            if(upperLimit==Util.getMax(inputData)){
                spaces++;
            }

            for (double speed : inputData) {
                for (double limit = lowerLimit; limit <= upperLimit; limit += spaceSize) {
                    if (table.get(limit) == null) {
                        table.put(limit, 0.0);
                    }

                    if (speed >= limit && speed < (limit + spaceSize)) {
                        table.put(limit, table.get(limit) + 1);
                    }
                }
            }
            //Start the graph with zero
            result.add(new Entry(0, 0.0f));
            for (int i = 1; i <= spaces; i++) {
                float yvalue = 0.0f;
                for (int j = i-1; j >= 0; j--) {
                    double limit = lowerLimit + j * spaceSize;
                    yvalue += (float) (double) table.get(limit);
                }
                yvalue = yvalue/inputData.size()*100;
                result.add(new Entry(i, yvalue));
            }
            result.add(new Entry( (float)(spaces+1), 100));
        } catch (Exception e) {
            //TODO
        }
        return result;
    }

    public static void generateFreqChart(final ArrayList<Double> inputData, View barChartView) {

        BarChart barChart = (BarChart) barChartView;
//        barChart.setMaxVisibleValueCount(60);
        barChart.setPinchZoom(false);
        barChart.setDrawBarShadow(false);
        barChart.setDrawGridBackground(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.animateY(2500);

        Description description = new Description();
        description.setText("Frequency vs Speed(km/h)");
        barChart.setDescription(description);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1.000f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return getXAxisLabel(inputData).get((int) value);
            }
        });

        YAxis yAxisLeft = barChart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setAxisMinimum(0.0f);
        yAxisLeft.setGranularity(1.000f);

        YAxis yAxisRight = barChart.getAxisRight();
        yAxisRight.setGranularityEnabled(true);
        yAxisRight.setAxisMinimum(0.0f);
        yAxisRight.setGranularity(1.000f);

        BarDataSet set1;

        if (barChart.getData() != null &&
                barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set1.setValues(generateFrequencyChartData(inputData));
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(generateFrequencyChartData(inputData), "Data Set");
            set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set1.setDrawValues(true);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
            barChart.setFitBars(true);
        }

        barChart.invalidate();
    }

    public static void generateHistoChart(final ArrayList<Double> inputData, View lineChartView) {

        LineChart lineChart = (LineChart) lineChartView;
        //barChart.setMaxVisibleValueCount(60);
        lineChart.setPinchZoom(false);
        lineChart.setDrawGridBackground(false);
        lineChart.getLegend().setEnabled(false);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.animateY(2500);

        Description description = new Description();
        description.setText("Percentage(%) vs Cumulative");
        lineChart.setDescription(description);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1.000f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return getXAxisLabel(inputData).get((int) value);
            }
        });

        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setGranularityEnabled(true);
        yAxisLeft.setGranularity(0.01f);
        yAxisLeft.setAxisMinimum(0.0f);

        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setGranularityEnabled(true);
        yAxisRight.setGranularity(0.01f);
        yAxisRight.setAxisMinimum(0.0f);

        LineDataSet set;

        if (lineChart.getData() != null &&
                lineChart.getData().getDataSetCount() > 0) {
            set = (LineDataSet) lineChart.getData().getDataSetByIndex(0);
            set.setValues(generateHistoChartData(inputData));
            lineChart.getData().notifyDataChanged();
            lineChart.notifyDataSetChanged();
        } else {
            set = new LineDataSet(generateHistoChartData(inputData), "Data Set");
            //set.setColors(ColorTemplate.VORDIPLOM_COLORS);
            set.setDrawValues(true);
            set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            set.setCubicIntensity(0.1f);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set);

            LineData data = new LineData(dataSets);
            lineChart.setData(data);
            //lineChart.setFitBars(true);
        }
        lineChart.invalidate();
    }

}
