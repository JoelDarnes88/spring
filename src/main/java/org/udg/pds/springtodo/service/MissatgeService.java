package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Missatge;
import org.udg.pds.springtodo.entity.Solicitud;
import org.udg.pds.springtodo.repository.MissatgeRepository;
import org.udg.pds.springtodo.repository.SolicitudRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MissatgeService {

    @Autowired
    MissatgeRepository missatgeRepository;

    @Autowired
    SolicitudRepository solicitudRepository;

    @Transactional
    public Missatge addMissatge(Long userId, Long solicitudId, String text, Boolean propietari) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new ServiceException("Solicitud not found"));

        // Verificar que l'usuari té permisos per afegir missatges a aquesta sol·licitud si és necessari

        Missatge missatge = new Missatge();
        missatge.setMissatge(text);
        missatge.setDataEnviament(LocalDateTime.now());
        missatge.setPropietari(propietari);
        missatge.setSolicitud(solicitud);

        missatgeRepository.save(missatge);
        return missatge;
    }

    public List<Missatge> getMissatgesFromSolicitud(Long solicitudId) {
        return missatgeRepository.findBySolicitudId(solicitudId);
    }
}
