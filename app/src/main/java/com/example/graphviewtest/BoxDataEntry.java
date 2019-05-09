package com.example.graphviewtest;

import com.anychart.chart.common.dataentry.DataEntry;

public class BoxDataEntry extends DataEntry {

    public BoxDataEntry(String x, Integer low, Integer q1, Integer median, Integer q3, Integer high) {
        setValue("x", x);
        setValue("low", low);
        setValue("q1", q1);
        setValue("median", median);
        setValue("q3", q3);
        setValue("high", high);
    }
}

class CustomBoxDataEntry extends BoxDataEntry {
    CustomBoxDataEntry(String x, Integer low, Integer q1, Integer median, Integer q3, Integer high, Integer[] outliers) {
        super(x, low, q1, median, q3, high);
        setValue("outliers", outliers);
    }
}