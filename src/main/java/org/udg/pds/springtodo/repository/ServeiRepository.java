package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Servei;

@Component
public interface ServeiRepository extends JpaRepository<Servei, Long> {
    Servei findByNameIgnoreCase(String name);
}
