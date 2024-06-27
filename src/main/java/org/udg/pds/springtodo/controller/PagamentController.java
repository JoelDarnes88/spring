package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.PagamentService;
import org.udg.pds.springtodo.service.UserService;

import java.util.List;

@RequestMapping(path = "/pagaments")
@RestController
public class PagamentController extends BaseController {

    @Autowired
    PagamentService pagamentService;

    @Autowired
    UserService userService;

    @PostMapping(consumes = "application/json")
    public ResponseEntity<String> addPagament(HttpSession session, @RequestBody PagamentRequest pagamentRequest) {
        Long userId = getLoggedUser(session); // Validate that the user is authenticated
        String paymentMethod = userService.getPaymentMethod(userId); // Get payment method of the logged-in user
        pagamentService.addPagamentToChat(pagamentRequest.chatId, userId, pagamentRequest.amount, paymentMethod);
        return ResponseEntity.ok(BaseController.OK_MESSAGE);
    }

    @GetMapping(path = "/{id}")
    @JsonView(Views.Public.class)
    public ResponseEntity<Pagament> getPagament(HttpSession session, @PathVariable("id") Long id) {
        Long userId = getLoggedUser(session);
        Pagament pagament = pagamentService.getPagament(id, userId);
        return ResponseEntity.ok(pagament);
    }

    @GetMapping
    @JsonView(Views.Public.class)
    public ResponseEntity<List<Pagament>> getAllPagaments(HttpSession session) {
        Long userId = getLoggedUser(session);
        List<Pagament> pagaments = pagamentService.getAllPagaments(userId);
        return ResponseEntity.ok(pagaments);
    }

    @PostMapping("/{id}/status")
    public ResponseEntity<?> updatePagamentStatus(HttpSession session, @PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        Long userId = getLoggedUser(session);
        Pagament updatedPagament = pagamentService.updatePagamentStatus(id, userId, request.status);
        return ResponseEntity.ok(updatedPagament);
    }

    static class StatusUpdateRequest {
        public Pagament.PaymentStatus status;
    }

    static class PagamentRequest {
        public Long chatId;
        public Double amount;
    }
}
