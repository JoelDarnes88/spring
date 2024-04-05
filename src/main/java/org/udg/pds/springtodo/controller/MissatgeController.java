package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Missatge;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.MissatgeService;

import java.util.List;

@RequestMapping(path="/missatges")
@RestController
public class MissatgeController extends BaseController {

    @Autowired
    private MissatgeService missatgeService;

    @PostMapping(consumes = "application/json")
    public String addMissatge(HttpSession session, @RequestBody MissatgeRequest missatgeRequest) {
        Long userId = getLoggedUser(session);
        missatgeService.addMissatge(userId, missatgeRequest.solicitudId, missatgeRequest.text, missatgeRequest.propietari);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{solicitudId}")
    @JsonView(Views.Public.class)
    public List<Missatge> getMissatgesFromSolicitud(HttpSession session, @PathVariable("solicitudId") Long solicitudId) {
        getLoggedUser(session); // Verifica si l'usuari està autenticat
        return missatgeService.getMissatgesFromSolicitud(solicitudId);
    }

    static class MissatgeRequest {
        public Long solicitudId;
        public String text;
        public Boolean propietari; // Pot ser null si no és necessari o no es vol especificar qui envia el missatge
    }
}
