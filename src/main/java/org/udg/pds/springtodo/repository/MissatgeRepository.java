package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Missatge;

import java.util.List;

@Component
public interface MissatgeRepository extends JpaRepository<Missatge, Long> {

    // Troba missatges per solicitud
    List<Missatge> findBySolicitudId(Long solicitudId);

}
