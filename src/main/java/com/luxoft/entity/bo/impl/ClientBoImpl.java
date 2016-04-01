package com.luxoft.entity.bo.impl;

import com.luxoft.entity.bo.ClientBO;
import com.luxoft.entity.dao.ClientDao;
import com.luxoft.entity.model.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by iivaniv on 03.07.2015.
 */

public class ClientBoImpl implements ClientBO {


    ClientDao clientDao;

    @Override
    public void save(Client client) {
        this.clientDao = clientDao;
    }

    @Override
    public void update(Client client) {
        clientDao.save(client);
    }

    @Override
    public void delete(Client client) {
        clientDao.delete(client);
    }

    @Override
    public Client findByClientId(Long id) {
        return clientDao.findByClientId(id);
    }
}
