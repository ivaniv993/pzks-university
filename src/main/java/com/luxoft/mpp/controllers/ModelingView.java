package com.luxoft.mpp.controllers;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.OhlcChartModel;
import org.primefaces.model.chart.OhlcChartSeries;

@ManagedBean
public class ModelingView implements Serializable {

    private OhlcChartModel ohlcModel;
    private OhlcChartModel ohlcModel2;

    @PostConstruct
    public void init() {
        createOhlcModels();
    }

    public OhlcChartModel getOhlcModel() {
        return ohlcModel;
    }

    public OhlcChartModel getOhlcModel2() {
        return ohlcModel2;
    }

    private void createOhlcModels() {
        createOhlcModel1();
        createOhlcModel2();
    }

    private void createOhlcModel1(){
        ohlcModel = new OhlcChartModel();

        ohlcModel.add(new OhlcChartSeries(2007, 130.52, 133.56, 126.04, 126.97));

        ohlcModel.add(new OhlcChartSeries(2007, 143.82, 144.56, 136.04, 136.97));
        ohlcModel.add(new OhlcChartSeries(2008, 138.7, 139.68, 135.18, 135.4));
        ohlcModel.add(new OhlcChartSeries(2009, 143.46, 144.66, 139.79, 140.02));
        ohlcModel.add(new OhlcChartSeries(2010, 140.67, 143.56, 132.88, 142.44));
        ohlcModel.add(new OhlcChartSeries(2011, 136.01, 139.5, 134.53, 139.48));
        ohlcModel.add(new OhlcChartSeries(2012, 124.76, 135.9, 124.55, 135.81));
        ohlcModel.add(new OhlcChartSeries(2012, 123.73, 129.31, 121.57, 122.5));

        ohlcModel.setTitle("OHLC Chart");
        ohlcModel.getAxis(AxisType.X).setLabel("Year");
        ohlcModel.getAxis(AxisType.Y).setLabel("Price Change $K/Unit");
    }

    private void createOhlcModel2(){
        ohlcModel2 = new OhlcChartModel();
        ohlcModel2.setAnimate(true);

        for( int i=1 ; i < 41 ; i++) {
            ohlcModel2.add(new OhlcChartSeries(i, Math.random() * 80 + 80, Math.random() * 50 + 110, Math.random() * 20 + 80, Math.random() * 80 + 80));
        }
        ohlcModel2.add(new OhlcChartSeries(1,
                 30,
                 50,
                 80,
                 90));


        ohlcModel2.setTitle("Candlestick");
        ohlcModel2.setCandleStick(true);
        ohlcModel2.getAxis(AxisType.X).setLabel("Sector");
        ohlcModel2.getAxis(AxisType.Y).setLabel("Index Value");
    }

}
