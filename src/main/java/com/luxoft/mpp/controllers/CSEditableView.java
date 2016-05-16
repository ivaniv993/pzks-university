package com.luxoft.mpp.controllers;

/**
 * Created by iivaniv on 31.03.2016.
 */

import com.luxoft.mpp.service.TaskService;
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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ManagedBean(name = "editableCSView")
@ViewScoped
public class CSEditableView implements Serializable {

    private DefaultDiagramModel model;

    private boolean suspendEvent;

    private Element sourceElement, targetElement;

    public TaskService getTaskServiceImpl() {
        return taskServiceImpl;
    }

    public void setTaskServiceImpl(TaskService taskServiceImpl) {
        this.taskServiceImpl = taskServiceImpl;
    }

    @ManagedProperty("#{taskServiceImpl}")
    private TaskService taskServiceImpl;

    private int[][] lm = new int[0][0];
    private Integer[] vertex = new Integer[0];

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
    }

    public DiagramModel getModel() {
        return model;
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

        if (!taskServiceImpl.findHangingVertex(lm).isEmpty() || !taskServiceImpl.testCSGraph(lm)){
            FacesContext context = FacesContext.getCurrentInstance();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Warning", "Your graph have handing nodes "));
            RequestContext.getCurrentInstance().update("form:msgs");

        }else{
            FacesContext context = FacesContext.getCurrentInstance();

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Success", "All is good"));
            RequestContext.getCurrentInstance().update("form:msgs");
        }

    }

    public void onConnect(ConnectEvent event) {
        if(!suspendEvent) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Connected",
                    "From " + event.getSourceElement().getData()+ " To " + event.getTargetElement().getData());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            RequestContext.getCurrentInstance().update("form:msgs");
        }
        else {
            suspendEvent = false;
        }
        updateTaskModel(((NetworkElement)event.getSourceElement().getData()).getId(),
                ((NetworkElement)event.getTargetElement().getData()).getId(), 777);
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

    public void addVertex( ){

        String x = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientX");
        String y = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("clientY");

        int xcord = Integer.valueOf(x);
        int ycord = Integer.valueOf(y)-200;

        NetworkElement networkElement = new NetworkElement(id++);

        Element element = new Element(networkElement, xcord+"px", ycord+"px");
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

        updateVertex();

        RequestContext.getCurrentInstance().update("form");

//        orderVertex(model);
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

    public void updateVertex( ){

        List<Integer> v = new ArrayList<Integer>();
        Collections.addAll(v, vertex);

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

            lm[source][target] = 777;
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

    public class NetworkElement implements Serializable {

        private int id;

        public NetworkElement() {
        }

        public NetworkElement(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return String.valueOf(id);
        }

    }
}
