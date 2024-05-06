package org.udg.pds.springtodo.entity;

import jakarta.persistence.*;
import lombok.Setter;
import org.udg.pds.springtodo.DTO.MessageBasicDTO;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Entity(name = "chat")
public class Chat implements Serializable {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "userTarget", nullable = false)
    private User targetUser;

    @ManyToOne
    @JoinColumn(name = "post", nullable = false)
    private Post post;

    @OneToMany(mappedBy = "chat")
    private List<Message> messages;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }

    public Post getPost() {
        return post;
    }

    public void setpost(Post post) {
        this.post = post;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public List<MessageBasicDTO> getMessagesDTO() {
        return messages.stream().map(MessageBasicDTO::fromEntity).collect(Collectors.toList());
    }
}
