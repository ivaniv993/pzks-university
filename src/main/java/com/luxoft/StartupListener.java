package com.luxoft;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.w3c.dom.DOMConfiguration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Created by iivaniv on 08.04.2016.
 */
public class StartupListener implements ServletContextListener {


    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        String logFile = servletContextEvent.getServletContext().getInitParameter("log4jFileName");

        DOMConfigurator.configure(servletContextEvent.getServletContext().getRealPath(logFile));
        Logger logger = LogManager.getLogger(StartupListener.class.getName());
        logger.debug("Loaded");
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
