package org.udg.pds.springtodo.DTO;

import org.udg.pds.springtodo.entity.Message;
import org.udg.pds.springtodo.entity.Post;

import java.time.LocalDateTime;
import java.util.List;

public class MessageBasicDTO {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private Long senderId;



    public static MessageBasicDTO fromEntity(Message m) {
        MessageBasicDTO dto = new MessageBasicDTO();
        dto.setId(m.getId());
        dto.setContent(m.getContent());
        dto.setTimestamp(m.getTimestamp());
        dto.setSenderId(m.getSender().getId());
        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getSenderId() {
        return senderId;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }
}
