package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "pagaments")
public class Pagament implements Serializable {
    private static final long serialVersionUID = 1L;

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        CANCELLED,
        REFUNDED
    }

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.Public.class)
    private Long id;

    @Setter
    @NotNull
    @Column(name = "payment_method")
    @JsonView(Views.Public.class)
    private String paymentMethod;

    @Setter
    @NotNull
    @JsonView(Views.Public.class)
    private Double amount;

    @Setter
    @NotNull
    @Enumerated(EnumType.STRING)
    @JsonView(Views.Public.class)
    private PaymentStatus status;

    @Getter
    @Setter
    @ManyToOne
    @JsonView(Views.Public.class)
    private User user;

    @Getter
    @Setter
    @ManyToOne
    @JsonView(Views.Public.class)
    private User userTarget;

    @Getter
    @Setter
    @ManyToOne
    @JsonIgnore
    private Chat chat;

    public Long getId() {
        return id;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public User getUserTarget() {
        return userTarget;
    }

    public Chat getChat() {
        return chat;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserTarget(User userTarget) {
        this.userTarget = userTarget;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
    }
}
