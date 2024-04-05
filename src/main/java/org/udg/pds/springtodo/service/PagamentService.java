package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.Solicitud;
import org.udg.pds.springtodo.repository.PagamentRepository;
import org.udg.pds.springtodo.repository.SolicitudRepository;

import java.util.Optional;

@Service
public class PagamentService {

    @Autowired
    private PagamentRepository pagamentRepository;

    @Autowired
    private SolicitudRepository solicitudRepository;

    @Transactional
    public Pagament addPagament(Long userId, Long solicitudId, Double preuFinal, Integer metode) {
        Solicitud solicitud = solicitudRepository.findById(solicitudId)
            .orElseThrow(() -> new ServiceException("Solicitud not found"));

        // Verificar que l'usuari té permisos per afegir pagaments a aquesta sol·licitud si és necessari

        Pagament pagament = new Pagament();
        pagament.setPreuFinal(preuFinal);
        pagament.setMetode(metode);
        pagament.setSolicitud(solicitud);

        solicitud.setPagament(pagament); // Asumint que Solicitud té un camp pagament per establir aquesta relació

        pagamentRepository.save(pagament);
        solicitudRepository.save(solicitud); // Guardar els canvis en la sol·licitud també, si es modifica la relació

        return pagament;
    }

    public Pagament getPagament(Long id, Long userId) {
        Optional<Pagament> pagament = pagamentRepository.findById(id);
        if (pagament.isEmpty() || !pagament.get().getSolicitud().getUsuari().getId().equals(userId))
            throw new ServiceException("Pagament does not exist or you don't have permission to view it");
        return pagament.get();
    }
}
