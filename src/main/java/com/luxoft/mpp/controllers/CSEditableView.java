package com.luxoft.mpp.controllers;

/**
 * Created by iivaniv on 31.03.2016.
 */

import org.primefaces.context.RequestContext;
import org.primefaces.event.diagram.ConnectEvent;
import org.primefaces.event.diagram.ConnectionChangeEvent;
import org.primefaces.event.diagram.DisconnectEvent;
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
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name = "editableCSView")
@ViewScoped
public class CSEditableView implements Serializable {

    private DefaultDiagramModel model;

    private boolean suspendEvent;

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
    }

    public DiagramModel getModel() {
        return model;
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
