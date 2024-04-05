package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.PagamentService;

@RequestMapping(path="/pagaments")
@RestController
public class PagamentController extends BaseController {

    @Autowired
    PagamentService pagamentService;

    @PostMapping(consumes = "application/json")
    public String addPagament(HttpSession session, @RequestBody PagamentRequest pagamentRequest) {
        Long userId = getLoggedUser(session);
        pagamentService.addPagament(userId, pagamentRequest.solicitudId, pagamentRequest.preuFinal, pagamentRequest.metode);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public Pagament getPagament(HttpSession session, @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);
        return pagamentService.getPagament(id, userId);
    }

    static class PagamentRequest {
        public Long solicitudId;
        public Double preuFinal;
        public Integer metode;
    }
}
