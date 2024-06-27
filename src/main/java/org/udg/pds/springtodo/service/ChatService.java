package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.DTO.ChatBasicDTO;
import org.udg.pds.springtodo.DTO.MessageBasicDTO;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.ChatRepository;
import org.udg.pds.springtodo.repository.MessageRepository;
import org.udg.pds.springtodo.repository.PagamentRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChatService {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PagamentRepository pagamentRepository;

    public Chat createChat(User user1, User user2, Post post) {
        Chat chat = new Chat();
        chat.setUser(user1);
        chat.setTargetUser(user2);
        chat.setPost(post);
        chat.setMessages(new ArrayList<>());
        return chatRepository.save(chat);
    }

    public List<Message> getMessages(Long chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow(() -> new ServiceException("Chat not found"));
        return messageRepository.findByChat(chat);
    }

    public Message sendMessage(Chat chat, User sender, String content) {
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public Chat findById(Long id) {
        Optional<Chat> ch = chatRepository.findById(id);
        if (ch.isPresent())
            return ch.get();
        else
            throw new ServiceException(String.format("Chat with id = %s does not exist", id));
    }

    public List<MessageBasicDTO> getMessagesDTO(Chat chat) {
        List<Message> mess = messageRepository.findByChat(chat);
        return mess.stream().map(MessageBasicDTO::fromEntity).collect(Collectors.toList());
    }

    public MessageBasicDTO sendMessageDTO(Chat chat, User sender, String content) {
        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return MessageBasicDTO.fromEntity(message);
    }

    public List<Chat> getUserChats(Long userId) {
        User user = userService.getUser(userId);
        return chatRepository.findByUserOrTargetUser(user, user);
    }

    public List<ChatBasicDTO> getUserChatsDTO(Long userId) {
        User user = userService.getUser(userId);
        List<Chat> chat = chatRepository.findByUserOrTargetUser(user, user);
        return chat.stream().map(ChatBasicDTO::fromEntity).collect(Collectors.toList());
    }

    public Optional<Chat> findChatByUsersAndPost(User user, User targetUser, Post post) {
        return chatRepository.findByUserAndTargetUserAndPost(user, targetUser, post)
            .or(() -> chatRepository.findByTargetUserAndUserAndPost(targetUser, user, post));
    }

    public void deleteChat(Chat chat) {
        chatRepository.delete(chat);
    }

    @Transactional
    public Pagament addPagamentToChat(Long chatId, Long userId, Double amount, String paymentMethod) {
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new ServiceException("Chat not found"));

        if (!chat.getUser().getId().equals(userId) && !chat.getTargetUser().getId().equals(userId)) {
            throw new ServiceException("You don't have permission to add a payment to this chat");
        }

        if (chat.getPagament() != null) {
            throw new ServiceException("A payment already exists for this chat");
        }

        Pagament pagament = new Pagament();
        pagament.setAmount(amount);
        pagament.setPaymentMethod(paymentMethod);
        pagament.setStatus(Pagament.PaymentStatus.PENDING);
        pagament.setUser(userService.getUser(userId));
        pagament.setChat(chat);

        chat.setPagament(pagament);

        pagamentRepository.save(pagament);
        chatRepository.save(chat);

        return pagament;
    }


    public Pagament getPagamentFromChat(Long chatId, Long userId) {
        Chat chat = findById(chatId);
        Pagament pagament = chat.getPagament();
        if (pagament == null || (!chat.getUser().getId().equals(userId) && !chat.getTargetUser().getId().equals(userId))) {
            throw new ServiceException("Pagament does not exist or you don't have permission to view it");
        }
        return pagament;
    }

    @Transactional
    public Pagament updatePagamentStatus(Long chatId, Long userId, Pagament.PaymentStatus status) {
        Chat chat = findById(chatId);
        Pagament pagament = chat.getPagament();
        if (pagament == null || !chat.getTargetUser().getId().equals(userId)) {
            throw new ServiceException("You don't have permission to update the status of this payment");
        }

        pagament.setStatus(status);
        return pagamentRepository.save(pagament);
    }
}
