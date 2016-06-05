package com.luxoft.mpp.controllers;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.luxoft.mpp.entity.model.*;
import com.luxoft.mpp.service.ModelingService;
import com.luxoft.mpp.service.TaskService;
import javafx.print.Collation;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.OhlcChartModel;
import org.primefaces.model.chart.OhlcChartSeries;

@ManagedBean(name = "modelingView")
@ViewScoped
public class ModelingView implements Serializable {

    private OhlcChartModel ohlcModel;
    private OhlcChartModel ohlcModel2;

    private List<SimpleMetaData> queueVariant;

    @ManagedProperty("#{taskServiceImpl}")
    private TaskService taskServiceImpl;

    @ManagedProperty("#{modelingServiceImpl}")
    private ModelingService modelingServiceImpl;

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
//        createOhlcModel2();
    }

    private void createOhlcModel1(){
        //init data
        queueVariant = new ArrayList<SimpleMetaData>();
        int[][] lm = {
                {0,1,0,2,0},
                {0,0,0,2,3},
                {0,0,0,0,3},
                {0,0,0,0,0},
                {0,0,0,0,0}
        };
        Integer[] vertex = new Integer[5];
        vertex[0] = 4;
        vertex[1] = 2;
        vertex[2] = 5;
        vertex[3] = 3;
        vertex[4] = 4;

        List<Task> taskGraph = modelingServiceImpl.createTaskGraph(lm, vertex);
        List<Processor> computerGraph = modelingServiceImpl.createMockCS();
        computerGraph = modelingServiceImpl.sortByConnectivity(computerGraph);

        queueVariant = taskServiceImpl.getQueueVariant3(lm, vertex);


        ohlcModel = new OhlcChartModel();

        ohlcModel.add(new OhlcChartSeries(1, 130.52, 133.56, 126.04, 126.97));
        ohlcModel.add(new OhlcChartSeries(1, 143.82, 144.56, 136.04, 136.97));
        ohlcModel.add(new OhlcChartSeries(2, 138.7, 139.68, 135.18, 135.4));
        ohlcModel.add(new OhlcChartSeries(3, 143.46, 144.66, 139.79, 140.02));
        ohlcModel.add(new OhlcChartSeries(4, 140.67, 143.56, 132.88, 142.44));
        ohlcModel.add(new OhlcChartSeries(4, 136.01, 139.5, 134.53, 139.48));
        ohlcModel.add(new OhlcChartSeries(5, 124.76, 135.9, 124.55, 135.81));
        ohlcModel.add(new OhlcChartSeries(5, 123.73, 129.31, 121.57, 122.5));

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


    public TaskService getTaskServiceImpl() {
        return taskServiceImpl;
    }


    public void setTaskServiceImpl(TaskService taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

    public ModelingService getModelingServiceImpl() {
        return modelingServiceImpl;
    }

    public void setModelingServiceImpl(ModelingService modelingServiceImpl) {
        this.modelingServiceImpl = modelingServiceImpl;
    }







}
