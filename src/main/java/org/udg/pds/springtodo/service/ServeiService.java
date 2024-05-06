package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Servei;
import org.udg.pds.springtodo.repository.ServeiRepository;

import java.util.List;

@Service
public class ServeiService {

    @Autowired
    private ServeiRepository serveiRepository;

    public List<Servei> getServices(){
        List<Servei> services = serveiRepository.findAll();
        if (!services.isEmpty()) return services;
        else throw new ServiceException("No services found");
    }

    @Transactional
    public Servei addService(String name, String description) {
        Servei servei = new Servei(name, description);
        serveiRepository.save(servei);
        return servei;
    }

    @Transactional
    public Servei searchService(String nomServei) {
        return serveiRepository.findByNameIgnoreCase(nomServei);
    }
}
