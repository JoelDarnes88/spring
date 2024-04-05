package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Solicitud;
import org.udg.pds.springtodo.service.SolicitudService;
import org.udg.pds.springtodo.entity.Views;

import java.util.List;

@RequestMapping(path="/solicituds")
@RestController
public class SolicitudController extends BaseController {

    @Autowired
    SolicitudService solicitudService;

    @GetMapping
    @JsonView(Views.Public.class)
    public List<Solicitud> listAllSolicituds(HttpSession session) {
        Long userId = getLoggedUser(session);
        return solicitudService.getAllSolicituds(userId);
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public Solicitud getSolicitud(HttpSession session, @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);
        return solicitudService.getSolicitud(id, userId);
    }

    @PostMapping(consumes = "application/json")
    public String addSolicitud(HttpSession session, @RequestBody SolicitudRequest solicitudRequest) {
        Long userId = getLoggedUser(session);
        solicitudService.addSolicitud(userId, solicitudRequest.postId, solicitudRequest.estat);
        return BaseController.OK_MESSAGE;
    }

    static class SolicitudRequest {
        public Long postId;
        public String estat;
    }
}
