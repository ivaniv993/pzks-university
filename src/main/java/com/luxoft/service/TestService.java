package com.luxoft.service;

import com.luxoft.entity.model.Client;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by iivaniv on 02.07.2015.
 */

@Service
public class TestService{

    @Transactional
    public Client saveClient(Client client){
        System.out.println("RegistrationUserBean:: Registering user " + client.getFirstName() + " " + client.getLastName());
        return client;
    }

}
