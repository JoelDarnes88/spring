package org.udg.pds.springtodo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.DTO.ChatBasicDTO;
import org.udg.pds.springtodo.DTO.MessageBasicDTO;
import org.udg.pds.springtodo.Global;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Message;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.service.ChatService;
import org.udg.pds.springtodo.service.PagamentService;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.UserService;

import java.util.List;
import java.util.Optional;

@RequestMapping(path = "/chats")
@RestController
public class ChatController extends BaseController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private PagamentService pagamentService;

    @Autowired
    private Global global;

    @PostMapping(path="/create")
    public ResponseEntity<?> createChat(HttpSession session, @RequestBody CreateChatRequest createChatRequest) {
        User user = userService.getUser(createChatRequest.userId);
        User targetUser = userService.getUser(createChatRequest.userTargetId);
        Post post = postService.getPost(createChatRequest.postId);

        Optional<Chat> existingChat = chatService.findChatByUsersAndPost(user, targetUser, post);
        if (existingChat.isPresent()) {
            return ResponseEntity.ok(existingChat.get());
        } else {
            Chat newChat = chatService.createChat(user, targetUser, post);
            return ResponseEntity.ok(newChat);
        }
    }

    @PostMapping("/{chatId}/addPagament")
    public ResponseEntity<Pagament> addPagamentToChat(HttpSession session, @PathVariable Long chatId, @RequestBody PagamentRequest pagamentRequest) {
        Long userId = getLoggedUser(session); // Ensure the user is authenticated
        String paymentMethod = userService.getPaymentMethod(userId); // Get payment method of the logged-in user
        Pagament pagament = pagamentService.addPagamentToChat(chatId, userId, pagamentRequest.amount, paymentMethod);
        return ResponseEntity.ok(pagament);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<List<MessageBasicDTO>> getMessages(@PathVariable Long chatId) {
        Chat c = chatService.findById(chatId);
        List<MessageBasicDTO> messages = chatService.getMessagesDTO(c);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{chatId}/send")
    public ResponseEntity<MessageBasicDTO> sendMessage(@PathVariable Long chatId, @RequestParam Long senderId, @RequestParam String content) {
        Chat chat = chatService.findById(chatId);
        User sender = userService.getUser(senderId);
        MessageBasicDTO message = chatService.sendMessageDTO(chat, sender, content);
        return ResponseEntity.ok(message);
    }

    @GetMapping("/user/me")
    public ResponseEntity<List<ChatBasicDTO>> getUserChats(HttpSession session) {
        try {
            Long id = getLoggedUser(session);
            List<ChatBasicDTO> chats = chatService.getUserChatsDTO(id);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/delete/{chatId}")
    public ResponseEntity<?> deleteChat(@PathVariable Long chatId) {
        Chat chat = chatService.findById(chatId);
        if (chat != null) {
            chatService.deleteChat(chat);
            return ResponseEntity.ok("Chat eliminat");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chat no trobat");
        }
    }

    static class CreateChatRequest {
        public Long userId;
        public Long userTargetId;
        public Long postId;
    }

    static class PagamentRequest {
        public Long chatId;
        public Double amount;
    }

}
