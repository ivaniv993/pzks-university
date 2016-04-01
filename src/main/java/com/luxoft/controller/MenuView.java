package com.luxoft.controller;

import com.luxoft.entity.model.Client;

import com.luxoft.service.ClientService;
import com.luxoft.service.TestService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.apache.log4j.Logger;

/**
 * Created by iivaniv on 30.06.2015.
 */
@ManagedBean
@ViewScoped
public class MenuView {

    private static final Logger log = Logger.getLogger(MenuView.class);

    @ManagedProperty("#{testService}")
    private TestService testService;

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    @ManagedProperty("#{clientService}")
    private ClientService clientService;

    private String message;


    public String save() {

        Client client = new Client("ivan", "ivanov", "6893467542");
        System.out.println("RegistrationUserBean:: Registering user " + client.getFirstName() + " " + client.getLastName());

        testService.saveClient(client);
        clientService.saveClient(client);
        return "welcome";
    }


    public TestService getTestService() {
        return testService;
    }

    public void setTestService(TestService testService) {
        this.testService = testService;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
