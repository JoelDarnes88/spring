package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.repository.ChatRepository;
import org.udg.pds.springtodo.repository.PagamentRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PagamentService {

    @Autowired
    private PagamentRepository pagamentRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserService userService;

    @Transactional
    public Pagament addPagamentToChat(Long chatId, Long userId, Double amount, String paymentMethod) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ServiceException("Chat not found"));

        Post post = chat.getPost();

        // Verificar si el usuario es el propietario del post
        if (!post.getUser().getId().equals(userId)) {
            throw new ServiceException("You don't have permission to add a payment to this chat");
        }

        // Verificar si ya existe un pago para este chat
        if (pagamentRepository.existsByChat(chat)) {
            throw new ServiceException("A payment already exists for this chat");
        }

        Pagament pagament = new Pagament();
        pagament.setAmount(amount);
        pagament.setPaymentMethod(paymentMethod);
        pagament.setStatus(Pagament.PaymentStatus.PENDING);
        pagament.setUser(chat.getUser()); // The user who creates the payment
        pagament.setUserTarget(chat.getTargetUser()); // The user who is the target of the payment
        pagament.setChat(chat);

        chat.setPagament(pagament);

        pagamentRepository.save(pagament);
        chatRepository.save(chat);

        return pagament;
    }

    public Pagament getPagament(Long id, Long userId) {
        Pagament pagament = pagamentRepository.findById(id)
            .orElseThrow(() -> new ServiceException("Pagament not found"));

        if (!pagament.getUser().getId().equals(userId) && !pagament.getUserTarget().getId().equals(userId)) {
            throw new ServiceException("You don't have permission to view this payment");
        }

        return pagament;
    }

    @Transactional
    public Pagament updatePagamentStatus(Long pagamentId, Long userId, Pagament.PaymentStatus status) {
        Pagament pagament = pagamentRepository.findById(pagamentId)
            .orElseThrow(() -> new ServiceException("Pagament not found"));

        Chat chat = pagament.getChat();
        Post post = chat.getPost();

        // Verificación del propietario del post
        if (!post.getUser().getId().equals(userId)) {
            throw new ServiceException("You don't have permission to update this payment status");
        }

        pagament.setStatus(status);
        return pagamentRepository.save(pagament);
    }

    public List<Pagament> getAllPagaments(Long userId) {
        return pagamentRepository.findAll().stream()
            .filter(p -> p.getUser().getId().equals(userId) || p.getUserTarget().getId().equals(userId))
            .collect(Collectors.toList());
    }
}
