package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.DTO.ChatBasicDTO;
import org.udg.pds.springtodo.DTO.MessageBasicDTO;
import org.udg.pds.springtodo.DTO.PostBasicDTO;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.*;

import java.time.LocalDateTime;
import java.util.Collection;
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
    UserService userService;

    ChatService chatService;
    public Chat createChat(User user1, User user2, Post post) {
        Chat chat = new Chat();
        chat.setUser(user1);
        chat.setTargetUser(user2);
        chat.setpost(post);
        return chatRepository.save(chat);
    }

    public List<Message> getMessages(Long chatId) {
        Chat chat = chatService.findById(chatId);
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
            throw new ServiceException(String.format("Chat with id = % does not exists", id));
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
        MessageBasicDTO mess = MessageBasicDTO.fromEntity(message);
        return mess;

    }


    public List<Chat> getUserChats(Long userId) {
        User user = userService.getUser(userId);
        return chatRepository.findByUserOrTargetUser(user, user);
    }

    public List<ChatBasicDTO> getUserChatsDTO(Long userId) {
        User user = userService.getUser(userId);
        List<Chat> chat = chatRepository.findByUserOrTargetUser(user,user);
        return chat.stream().map(ChatBasicDTO::fromEntity).collect(Collectors.toList());
    }

}
