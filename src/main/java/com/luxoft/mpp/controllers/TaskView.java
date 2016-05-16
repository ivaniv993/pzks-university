package com.luxoft.mpp.controllers;

import com.luxoft.mpp.entity.model.SimpleVertex;
import com.luxoft.mpp.entity.model.TaskElement;
import com.luxoft.mpp.entity.model.SimpleMetaData;
import com.luxoft.mpp.service.TaskService;
import com.luxoft.mpp.service.TaskServiceImpl;
import com.luxoft.mpp.utils.LRUCache;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
import org.primefaces.model.diagram.Connection;
import org.primefaces.model.diagram.DefaultDiagramModel;
import org.primefaces.model.diagram.DiagramModel;
import org.primefaces.model.diagram.Element;
import org.primefaces.model.diagram.connector.StraightConnector;
import org.primefaces.model.diagram.endpoint.DotEndPoint;
import org.primefaces.model.diagram.endpoint.EndPoint;
import org.primefaces.model.diagram.endpoint.EndPointAnchor;
import org.primefaces.model.diagram.endpoint.RectangleEndPoint;
import org.primefaces.model.diagram.overlay.ArrowOverlay;
import org.primefaces.model.diagram.overlay.LabelOverlay;
import org.primefaces.model.diagram.overlay.Overlay;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.*;

/**
 * Created by iivaniv on 18.03.2016.
 *
 */
@ManagedBean(name = "diagramEditableView")
@ViewScoped
public class TaskView implements Serializable {

    final static Logger logger = Logger.getLogger(TaskView.class);

    private DefaultDiagramModel model;

    private boolean suspendEvent;

    private int taskDuration;

    private int linkDuration;

    private String description;

    private Element sourceElement, targetElement;

    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];

    private List<SimpleMetaData> queueVariant;

    private int minTaskValue = 5;
    private int maxTaskValue = 10;
    private int vertexQuantity = 4;
    private double correlation;
    private int minLoopValue = 20;
    private int maxLoopValue = 30;

    private LRUCache<Integer, Connection> connectionLRUCache = new LRUCache<Integer, Connection>(3);

    private LRUCache<Integer, TaskElement> taskCache = new LRUCache<Integer, TaskElement>(3);

    @ManagedProperty("#{taskServiceImpl}")
    private TaskService taskServiceImpl;

    private static int id=0;

    @PostConstruct
    public void init() {
        queueVariant = new ArrayList<SimpleMetaData>();

        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);

        id = 0;

        logger.info("Init bean (View scope)");
    }

    public void generateGraph(){

        if (maxTaskValue < minTaskValue || maxLoopValue < minLoopValue
                || minTaskValue < 0 || maxTaskValue < 0 || maxLoopValue < 0 || minLoopValue < 0) {
            FacesContext context = FacesContext.getCurrentInstance();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Warning", "Wrong parameter "));
            RequestContext.getCurrentInstance().update("form:msgs");
            return;
        }

        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);

        vertex = new Integer[vertexQuantity];

        id = vertexQuantity;

        Random rand = new Random();
        for (int i = 0; i < vertex.length; i++) {
            vertex[i] = getValueFromRange(minTaskValue, maxTaskValue);
        }

        do {
            lm = new int[vertex.length][vertex.length];
            for (int i = 0; i < lm.length-1; i++) {

                int linkNumber = 1;
                while( linkNumber != 0) {

                    int linkValue = getValueFromRange(minLoopValue, maxLoopValue);

                    int randomVertex = rand.nextInt(lm[i].length);

                    if(randomVertex == i)
                        continue;
                    if (lm[i][randomVertex] != 0)
                        continue;

                    lm[i][randomVertex] = linkValue;

                    linkNumber--;
                }

            }
        } while( taskServiceImpl.isLoop(lm) || !taskServiceImpl.findHangingVertex(lm).isEmpty() || !taskServiceImpl.hasWayToLastVertex(lm) );

//        countCorrelation();

        System.out.println("Vertex");
        for (int e : vertex){
            System.out.print(e + ", ");
        }
        System.out.println("\n___________________");

        for (int i = 0; i < lm.length; i++) {
            for (int j = 0; j < lm[i].length; j++) {
                System.out.print(lm[i][j] + ", ");
            }
            System.out.println();
        }

        for (int i = 0; i < vertex.length; i++) {

            TaskElement taskElement = new TaskElement(i, vertex[i]);

            Element element = new Element(taskElement);
            EndPoint endPoint = createDotEndPoint(EndPointAnchor.AUTO_DEFAULT);
            element.setDraggable(true);
            endPoint.setTarget(true);
            element.addEndPoint(endPoint);

            EndPoint beginPoint = createRectangleEndPoint(EndPointAnchor.BOTTOM);
            beginPoint.setSource(true);
            element.addEndPoint(beginPoint);

            model.addElement(element);
        }

        for (int i = 0; i < lm.length; i++) {
            for (int j = 0; j < lm[i].length; j++) {
                if (lm[i][j] != 0){
                    Element source = getElementByDataId(i);
                    Element target = getElementByDataId(j);
                    Connection connection = createConnection(
                            source.getEndPoints().get(1),
                            target.getEndPoints().get(0), String.valueOf(lm[i][j]));

                    model.connect(connection);

                }
            }
        }
        orderVertex(model);
        RequestContext.getCurrentInstance().update("form");
        RequestContext.getCurrentInstance().update("generate_graph");

    }

    private Element getElementByDataId(int id){

        for ( Element e : model.getElements() ){
            if (((TaskElement)e.getData()).getId() == id)
                return e;
        }
        return null;
    }


    private int getValueFromRange( int min, int max ){
        Random rand = new Random();
        return min + rand.nextInt(max-min);
    }


    public void updateVertex( int duration, int id ){

        List<Integer> v = new ArrayList<Integer>();
        Collections.addAll(v, vertex);
        v.add(duration);

        vertex = new Integer[v.size()];
        v.toArray(vertex);


        List<Element> elements = getModel().getElements();
        List<Connection> connections = getModel().getConnections();
        lm = new int[elements.size()][elements.size()];

        String regex = "[^0-9]";
        for( Connection conn : connections){

            String sourceId = conn.getSource().getId().replaceAll(regex, "");
            String targetId = conn.getTarget().getId().replaceAll(regex, "");

            int source =  Integer.valueOf(sourceId);
            int target =  Integer.valueOf(targetId);

            lm[source][target] = duration;
        }
    }


    public void updateTaskModel(int from, int to, int value){

        if ( value < 0){
            throw new IllegalArgumentException(value +" can`t be lover zero ");
        }

        if ( from <= lm[0].length && from >= lm.length){
            throw new IllegalArgumentException(from +" out of array ");
        }

        if ( to <= lm[0].length && to >= lm.length){
            throw new IllegalArgumentException(to +" out of array ");
        }

        lm[from][to] = value;

    }


    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        if(label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
    }


    public void updateConnection(){

        Connection conn = connectionLRUCache.get(1);

        conn.setOverlays(Collections.<Overlay>singletonList(new LabelOverlay(String.valueOf(getLinkDuration()), "flow-label", 0.5)));
        updateTaskModel(((TaskElement) sourceElement.getData()).getId(), ((TaskElement) targetElement.getData()).getId(), getLinkDuration());
        RequestContext.getCurrentInstance().update("form");

    }


    public void updateAddTask(){

        updateVertex(taskDuration, id);
        TaskElement taskElement = taskCache.get(1);
        taskElement.setTaskDuration(taskDuration);
        RequestContext.getCurrentInstance().update("form");
    }


    public void addTask( ){

        String x = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientX");
        String y = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientY");

        int xcord = Integer.valueOf(x)-600;
        int ycord = Integer.valueOf(y)-200;

        TaskElement taskElement = new TaskElement(id++, taskDuration);
        taskCache.put(1, taskElement);

        Element element = new Element(taskElement, xcord+"px", ycord+"px");
        EndPoint endPoint = createDotEndPoint(EndPointAnchor.AUTO_DEFAULT);
        endPoint.setId(String.valueOf(id) + "_TARGET");
        element.setDraggable(true);
        endPoint.setTarget(true);
        element.addEndPoint(endPoint);

        EndPoint beginPoint = createRectangleEndPoint(EndPointAnchor.BOTTOM);
        beginPoint.setId(String.valueOf(id) + "_SOURCE");
        beginPoint.setSource(true);
        element.addEndPoint(beginPoint);

        model.addElement(element);

    }

    public void onConnect(ConnectEvent event) {
        if(!suspendEvent) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connected",
                    "From " + event.getSourceElement().getData()+ " To " + event.getTargetElement().getData());
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
        else {
            suspendEvent = false;
        }
        Connection connection = createConnection(event.getSourceElement().getEndPoints().get(1), event.getTargetElement().getEndPoints().get(0), "0");
        connectionLRUCache.put(1, connection);
        model.connect(connection);

        sourceElement = event.getSourceElement();
        targetElement = event.getTargetElement();

        RequestContext.getCurrentInstance().update("form");
    }


    public void onDisconnect(DisconnectEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Disconnected",
                "From " + event.getSourceElement().getData()+ " To " + event.getTargetElement().getData());

        FacesContext.getCurrentInstance().addMessage(null, msg);

        RequestContext.getCurrentInstance().update("form:msgs");
    }

    public void onConnectionChange(ConnectionChangeEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connection Changed",
                "Original Source:" + event.getOriginalSourceElement().getData() +
                        ", New Source: " + event.getNewSourceElement().getData() +
                        ", Original Target: " + event.getOriginalTargetElement().getData() +
                        ", New Target: " + event.getNewTargetElement().getData());

        FacesContext.getCurrentInstance().addMessage(null, msg);

        RequestContext.getCurrentInstance().update("form:msgs");
        suspendEvent = true;
    }

    public void orderVertex(DefaultDiagramModel model){

        List<Element> elements = model.getElements();
        int x = 15, y = 10;

        for( int i =0; i < elements.size(); i ++ ) {

            elements.get(i).setX(x + "px");
            elements.get(i).setY(y + "px");
            if (i != 0 && (i % 2 == 0) ) {

                x = 15;
                y += 200;
                continue;
            }
            x += 200;
        }

    }


    public void testGraph(){

        System.out.println("\n________Test graph___________");

        for (int i = 0; i < lm.length; i++) {
            for (int j = 0; j < lm[i].length; j++) {
                System.out.print(lm[i][j] + ", ");
            }
            System.out.println();
        }
        System.out.println("\n________Test graph___________");

        if (taskServiceImpl.isLoop(lm)){
            FacesContext context = FacesContext.getCurrentInstance();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Warning", "Your graph have loop "));
            RequestContext.getCurrentInstance().update("form:msgs");
            return;
        }

    }

    public void runLab2(){
        testGraph();
        queueVariant = taskServiceImpl.getQueueVariant3(lm, vertex);

    }

    public void runLab3(){
        testGraph();
        queueVariant = taskServiceImpl.getQueueVariant8(lm, vertex);

    }

    public void runLab4(){
        testGraph();
        queueVariant = taskServiceImpl.getQueueVariant13(lm, vertex);

    }

    public void onNewDiagram() {
        queueVariant = new ArrayList<SimpleMetaData>();

        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);

        id = 0;

        logger.info("On new diagram");

    }

    public void saveGraph(){
        System.out.println("save Graph");
    }

    private void countCorrelation(){

        int linkSumValue = 0;
        int vertexSumValue = 0 ;

        for ( int v : vertex )
            vertexSumValue += v;

        for ( int[] row : lm )
            for (int e : row)
                linkSumValue += e;

        correlation = (double)vertexSumValue/(vertexSumValue + linkSumValue);

    }

    private EndPoint createDotEndPoint(EndPointAnchor anchor) {
        DotEndPoint endPoint = new DotEndPoint(anchor);
        endPoint.setScope("network");
        endPoint.setTarget(true);
        endPoint.setStyle("{fillStyle:'#98AFC7'}");
        endPoint.setHoverStyle("{fillStyle:'#5C738B'}");

        return endPoint;
    }

    private EndPoint createRectangleEndPoint(EndPointAnchor anchor) {
        RectangleEndPoint endPoint = new RectangleEndPoint(anchor);
        endPoint.setScope("network");
        endPoint.setSource(true);
        endPoint.setStyle("{fillStyle:'#98AFC7'}");
        endPoint.setHoverStyle("{fillStyle:'#5C738B'}");

        return endPoint;
    }

    public DiagramModel getModel() {
        return model;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLinkDuration() {
        return linkDuration;
    }

    public void setLinkDuration(int linkDuration) {
        this.linkDuration = linkDuration;
    }

    public int getTaskDuration() {
        return taskDuration;
    }

    public void setTaskDuration(int taskDuration) {
        this.taskDuration = taskDuration;
    }

    public TaskService getTaskServiceImpl() {
        return taskServiceImpl;
    }

    public void setTaskServiceImpl(TaskService taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

    public List<SimpleMetaData> getQueueVariant() {
        return queueVariant;
    }

    public void setQueueVariant(List<SimpleMetaData> queueVariant) {
        this.queueVariant = queueVariant;
    }

    public int getMinTaskValue() {
        return minTaskValue;
    }

    public void setMinTaskValue(int minTaskValue) {
        this.minTaskValue = minTaskValue;
    }

    public int getMaxTaskValue() {
        return maxTaskValue;
    }

    public void setMaxTaskValue(int maxTaskValue) {
        this.maxTaskValue = maxTaskValue;
    }

    public int getVertexQuantity() {
        return vertexQuantity;
    }

    public void setVertexQuantity(int vertexQuantity) {
        this.vertexQuantity = vertexQuantity;
    }

    public double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(double correlation) {
        this.correlation = correlation;
    }

    public int getMinLoopValue() {
        return minLoopValue;
    }

    public void setMinLoopValue(int minLoopValue) {
        this.minLoopValue = minLoopValue;
    }

    public int getMaxLoopValue() {
        return maxLoopValue;
    }

    public void setMaxLoopValue(int maxLoopValue) {
        this.maxLoopValue = maxLoopValue;
    }


}



