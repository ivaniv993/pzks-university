package com.luxoft.mpp.controllers;

import com.luxoft.mpp.service.TaskService;
import com.luxoft.mpp.utils.LRUCache;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
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
import org.primefaces.push.EventBusFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
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


    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];

    private LRUCache<Integer, Connection> connectionLRUCache = new LRUCache<Integer, Connection>(3);

    @ManagedProperty("#{taskServiceImpl}")
    private TaskService taskServiceImpl;

    private static int id=0;

    @PostConstruct
    public void init() {
        model = new DefaultDiagramModel();
        model.setMaxConnections(-1);

        model.getDefaultConnectionOverlays().add(new ArrowOverlay(20, 20, 1, 1));
        StraightConnector connector = new StraightConnector();
        connector.setPaintStyle("{strokeStyle:'#98AFC7', lineWidth:3}");
        connector.setHoverPaintStyle("{strokeStyle:'#5C738B'}");
        model.setDefaultConnector(connector);

        id = 0;

        addTask();
        addTask();



        logger.info("Init bean (View scope)");
    }

    public void updateVertex( int duration, int id ){

        List<Integer> v = new ArrayList<Integer>();
        Collections.addAll(v, vertex);
        v.add(duration);
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


    public void updateConnection(){

        Connection conn = connectionLRUCache.get(1);

        conn.setOverlays(Collections.<Overlay>singletonList(new LabelOverlay(String.valueOf(getLinkDuration()), "flow-label", 0.5)));
        updateTaskModel(((NetworkElement) sourceElement.getData()).getId(), ((NetworkElement) targetElement.getData()).getId(), getLinkDuration());
        RequestContext.getCurrentInstance().update("form");

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

    private Element sourceElement, targetElement;

    private Connection createConnection(EndPoint from, EndPoint to, String label) {
        Connection conn = new Connection(from, to);
        conn.getOverlays().add(new ArrowOverlay(20, 20, 1, 1));

        if(label != null) {
            conn.getOverlays().add(new LabelOverlay(label, "flow-label", 0.5));
        }

        return conn;
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
        int x = 9, y = 6;

        for( int i =0; i < elements.size(); i ++ ) {

            elements.get(i).setX(x + "px");
            elements.get(i).setY(y + "px");
            if (i != 0 && i % 4 == 0) {

                x = 6;
                y += 9;

            }
            x += 9;
        }

    }


    public void addTask( ){

        String x = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientX");
        String y = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientY");

        Element element = new Element(new NetworkElement(id++, taskDuration), x+"px", y+"px");
        EndPoint endPoint = createDotEndPoint(EndPointAnchor.AUTO_DEFAULT);
        endPoint.setId(String.valueOf(id)+"_TARGET");
        element.setDraggable(true);
        endPoint.setTarget(true);
        element.addEndPoint(endPoint);

        EndPoint beginPoint = createRectangleEndPoint(EndPointAnchor.BOTTOM);
        beginPoint.setId(String.valueOf(id)+"_SOURCE");
        beginPoint.setSource(true);
        element.addEndPoint(beginPoint);

        model.addElement(element);

        updateVertex(taskDuration, id);

//        orderVertex(model);
    }


    public void run(){

        if (taskServiceImpl.isLoop(lm)){
            FacesContext context = FacesContext.getCurrentInstance();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Warning", "Your graph have loop "));
            RequestContext.getCurrentInstance().update("form:msgs");
            return;
        }
//        taskServiceImpl.saveVertex();

    }

    public void onNewDiagram() {
        logger.info("On new diagram");

    }

    public void saveGraph(){
        System.out.println("save Graph");
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

    public class NetworkElement implements Serializable {

        private int id;

        private int taskDuration;

        public NetworkElement() {
        }

        public NetworkElement(int id, int duratuion) {
            this.id = id;
            this.taskDuration = duratuion;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getTaskDuration() {
            return taskDuration;
        }

        public void setTaskDuration(int taskDuration) {
            this.taskDuration = taskDuration;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }

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

}



