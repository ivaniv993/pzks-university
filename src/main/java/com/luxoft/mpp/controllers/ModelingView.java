package com.luxoft.mpp.controllers;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.luxoft.mpp.entity.model.*;
import com.luxoft.mpp.service.GrantModelingService;
import com.luxoft.mpp.service.ModelingService;
import com.luxoft.mpp.service.TaskService;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.OhlcChartModel;
import org.primefaces.model.chart.OhlcChartSeries;

@ManagedBean(name = "modelingView")
@ViewScoped
public class ModelingView implements Serializable {

    private int[][] matrix =
                   {{0,1,0,0,0,1},//0
                    {1,0,1,1,0,0},//1
                    {0,1,0,1,1,0},//2
                    {0,1,1,0,1,1},//3
                    {0,0,1,1,0,1},//4
                    {1,0,0,1,1,0},//5
            };


    private OhlcChartModel ohlcModel;
    private OhlcChartModel ohlcModel2;

    private List<SimpleMetaData> queueVariant;

    @ManagedProperty("#{taskServiceImpl}")
    private TaskService taskServiceImpl;

    @ManagedProperty("#{modelingServiceImpl}")
    private ModelingService modelingServiceImpl;

    @ManagedProperty("#{grantModelingService}")
    private GrantModelingService grantModelingService;

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

        int[][] bufMatrix = new int[matrix.length][matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                for (int k = 0; k < matrix.length; k++) {
                    bufMatrix[j][k] = Math.min(bufMatrix[j][k], (matrix[j][i]+matrix[i][k]) );
                }
            }
        }

        System.out.println("\n________Test graph___________");

        for (int i = 0; i < bufMatrix.length; i++) {
            for (int j = 0; j < bufMatrix.length; j++) {
                System.out.print(bufMatrix[i][j] + ", ");
            }
            System.out.println();
        }

        System.out.println("\n________Test graph___________");

        createOhlcModel1();
//        createOhlcModel2();
    }

    public List<Processor> createMockCS(){

        List< Processor > result = new ArrayList<Processor>();

        int permit = 2;

        Processor
                p0 = new Processor(0),
                p1 = new Processor(1),
                p2 = new Processor(2),
                p3 = new Processor(3),
                p4 = new Processor(4),
                p5 = new Processor(5);



        ProcLink
                link01 = new ProcLink(0, 1, permit),
                link05 = new ProcLink(0, 5, permit),

                link12 = new ProcLink(1, 2, permit),
                link13 = new ProcLink(1, 3, permit),

                link23 = new ProcLink(2, 3, permit),
                link24 = new ProcLink(2, 4, permit),

                link34 = new ProcLink(3, 4, permit),
                link35 = new ProcLink(3, 5, permit),

                link45 = new ProcLink(4, 5, permit);

        Collections.addAll(p0.getLinks(), link01, link05 );
        Collections.addAll(p1.getLinks(), link01, link12, link13);
        Collections.addAll(p2.getLinks(), link12, link23, link24);
        Collections.addAll(p3.getLinks(), link13, link23, link34, link35);
        Collections.addAll(p4.getLinks(), link24, link34);
        Collections.addAll(p5.getLinks(), link35, link45);

        Collections.addAll(result, p0, p1, p2, p3, p4, p5);

        return result;
    }


    private void createOhlcModel1(){
        //init data
        queueVariant = new ArrayList<SimpleMetaData>();
        int[][] lm = {
                {0,0,0,0,2,0},
                {0,0,0,0,3,0},
                {0,0,0,0,4,0},
                {0,0,0,0,0,5},
                {0,0,0,0,0,6},
                {0,0,0,0,0,0}
        };
        Integer[] vertex = new Integer[6];
        vertex[0] = 1;
        vertex[1] = 2;
        vertex[2] = 3;
        vertex[3] = 4;
        vertex[4] = 5;
        vertex[5] = 6;

        int[][] matrix =
                {{0,1,0,0},//0
                 {1,0,1,0},//1
                 {0,1,0,1},//2
                 {0,0,1,0} //3
                };

        matrix = readFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/cs.txt");
        lm = readFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/ts.txt");
        vertex = readVertexFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/vertex.txt");

        List<Task> taskGraph = modelingServiceImpl.createTaskGraph(lm, vertex);
        List<Processor> computerGraph = modelingServiceImpl.createMockCS(matrix, 2);
        computerGraph = modelingServiceImpl.sortByConnectivity(computerGraph);

        queueVariant = taskServiceImpl.getQueueVariant3(lm, vertex);

        List<Processor> processors = grantModelingService.modeling(computerGraph, taskGraph, matrix, queueVariant);


        ohlcModel = new OhlcChartModel();

        for (Processor proc : processors){
            for (TimeUnit timeUnit : proc.getTimeLine() ){
                if (  ! timeUnit.isIdleTime())
//                    Task task = (Task)timeUnit;
                    ohlcModel.add(new OhlcChartSeries( proc.getID(),
                            (double)timeUnit.getTo(),
                            (double)timeUnit.getTo()+0.1,
                            (double)timeUnit.getFrom(),
                            (double)timeUnit.getFrom()+0.1 ));

            }

        }

        ohlcModel.setAnimate(true);
        ohlcModel.setCandleStick(true);
        ohlcModel.setTitle("Chart");
        ohlcModel.getAxis(AxisType.X).setLabel("Processors");
        ohlcModel.getAxis(AxisType.Y).setLabel("Time");
    }

    private void update(){

        matrix = readFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/cs.txt");
        int[][] lm = readFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/ts.txt");
        Integer[] vertex = readVertexFile("D://IdeaProjects/pzks/pzks-university/src/main/resources/vertex.txt");

        List<Task> taskGraph = modelingServiceImpl.createTaskGraph(lm, vertex);
        List<Processor> computerGraph = modelingServiceImpl.createMockCS(matrix, 2);
        computerGraph = modelingServiceImpl.sortByConnectivity(computerGraph);
    }


    private int[][] readFile(String path){
        int[][] result = new int[0][0];
        BufferedReader br = null;
        try{
            br = new BufferedReader(new FileReader(path));
            String sCurrentLine = null;
            while (sCurrentLine == null) {
                sCurrentLine = br.readLine();
                System.out.println(sCurrentLine);
            }

            String[] row = sCurrentLine.split(";");
            result = new int[row.length][row.length];
            for (int i = 0; i < row.length; i++) {
                String[] cell = row[i].split(",");
                for (int j = 0; j < cell.length; j++) {
                    result[i][j] = Integer.valueOf(cell[j]);
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Integer[] readVertexFile(String path){
        Integer[] result = new Integer[0];
        BufferedReader br = null;
        try {

            br = new BufferedReader(new FileReader(path));
            String sCurrentLine = null;
            while (sCurrentLine == null) {
                sCurrentLine = br.readLine();
                System.out.println(sCurrentLine);
            }

            String[] row = sCurrentLine.split(";");
            result = new Integer[row.length];
            for (int j = 0; j < row.length; j++) {
                result[j] = Integer.valueOf(row[j]);
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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

    public GrantModelingService getGrantModelingService() {
        return grantModelingService;
    }

    public void setGrantModelingService(GrantModelingService grantModelingService) {
        this.grantModelingService = grantModelingService;
    }

}
