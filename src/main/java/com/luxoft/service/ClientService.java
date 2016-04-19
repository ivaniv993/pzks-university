package com.luxoft.service;

import com.luxoft.entity.model.Client;
import com.luxoft.persistence.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by iivaniv on 01.07.2015.
 */

@Service
public class ClientService {

//    @Autowired
    ClientRepository clientRepository;

    public void saveClient(){
//        System.out.println("RegistrationUserBean:: Registering user " + client.getFirstName() + " " + client.getLastName());
//        return clientRepository.save(client);
    }
}
