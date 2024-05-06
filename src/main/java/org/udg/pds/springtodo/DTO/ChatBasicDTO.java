package org.udg.pds.springtodo.DTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Message;
import org.udg.pds.springtodo.service.ChatService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChatBasicDTO {
    private Long id;
    private Long userId;
    private Long userTargetId;
    private Long postId;
    private List<MessageBasicDTO> messages = new ArrayList<>();


    public static ChatBasicDTO fromEntity(Chat c) {
        ChatBasicDTO dto = new ChatBasicDTO();
        dto.setId(c.getId());
        dto.setUserId(c.getUser().getId());
        dto.setUserTargetId(c.getTargetUser().getId());
        dto.setPostId(c.getPost().getId());
        dto.setMessages(c.getMessagesDTO());
        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserTargetId() {
        return userTargetId;
    }

    public void setUserTargetId(Long userTargetId) {
        this.userTargetId = userTargetId;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public List<MessageBasicDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageBasicDTO> messages) {
        this.messages = messages;
    }
}
