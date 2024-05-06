package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.DTO.ChatBasicDTO;
import org.udg.pds.springtodo.DTO.MessageBasicDTO;
import org.udg.pds.springtodo.DTO.PostBasicDTO;
import org.udg.pds.springtodo.Global;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.ChatRepository;
import org.udg.pds.springtodo.repository.PostImageRepository;
import org.udg.pds.springtodo.repository.UserRepository;
import org.udg.pds.springtodo.service.ChatService;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.UserService;

import java.util.Collection;
import java.util.Date;
import java.util.List;

@RequestMapping(path = "/chats")
@RestController
public class ChatController extends BaseController {

    @Autowired
    private ChatService chatService;
    @Autowired
    private UserService userService;
    @Autowired
    private PostService postService;
    UserRepository userRepository;
    ChatRepository chatRepository;

    @Autowired
    Global global;

    @PostMapping(path="/create")
    public ResponseEntity<?> createChat(HttpSession session,
                                        @RequestParam("user1Id") Long user1Id,
                                        @RequestParam("user2Id") Long user2Id,@RequestParam("postId") Long postId) {
        User user1 = userService.getUser(user1Id);
        User user2 = userService.getUser(user2Id);
        Post p = postService.getPost(postId);
        Chat chat = chatService.createChat(user1, user2, p);
        return ResponseEntity.ok("Chat creat OK!");
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
    //@JsonView(Views.Public.class)
    public ResponseEntity<List<ChatBasicDTO>> getUserChats(HttpSession session) {
        try {
            Long id = getLoggedUser(session);
            List<ChatBasicDTO> chats = chatService.getUserChatsDTO(id);
            return ResponseEntity.ok(chats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
