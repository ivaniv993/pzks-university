package com.luxoft.entity.bo;

import com.luxoft.entity.model.Client;

/**
 * Created by iivaniv on 03.07.2015.
 */
public interface ClientBO {

    void save(Client stock);
    void update(Client stock);
    void delete(Client stock);
    Client findByClientId(Long id);
}
