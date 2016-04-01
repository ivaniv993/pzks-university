package com.luxoft.persistence.repository;

import com.luxoft.entity.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by iivaniv on 01.07.2015.
 */
public interface ClientRepository extends JpaRepository<Client, Long> {
}
