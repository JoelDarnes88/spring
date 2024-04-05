package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Solicitud;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.SolicitudRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SolicitudService {

    @Autowired
    SolicitudRepository solicitudRepository;

    @Autowired
    UserRepository userRepository;

    public List<Solicitud> getAllSolicituds(Long userId) {
        // Aquí podríem filtrar les sol·licituds basant-nos en l'usuari si fos necessari
        return solicitudRepository.findAll();
    }

    public Solicitud getSolicitud(Long id, Long userId) {
        Optional<Solicitud> solicitud = solicitudRepository.findById(id);
        if (solicitud.isEmpty() || !solicitud.get().getUsuari().getId().equals(userId))
            throw new ServiceException("Solicitud does not exist or you don't have permission to view it");
        return solicitud.get();
    }

    @Transactional
    public Solicitud addSolicitud(Long userId, Long postId, String estat) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new ServiceException("User does not exist");

        Solicitud solicitud = new Solicitud();
        solicitud.setUsuari(user.get());
        solicitud.setEstat(estat);
        // Aquí faltaria establir la relació amb el Post, i possiblement inicialitzar altres camps com Pagament o Missatges

        solicitudRepository.save(solicitud);
        return solicitud;
    }

}
