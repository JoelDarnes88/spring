package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Solicitud;

@Component
public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    // List<Solicitud> findByEstat(String estat);
    // List<Solicitud> findByUsuariId(Long usuariId);
}
