package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.ArrayList;
import java.util.Date;


@Entity
@Table(name = "tokens")
public class Token {

    public Token() {
        this.creation = new Date();
    }

    public Token(String token, Long user_id) {
        this.token = token;
        this.user_id = user_id;
        this.creation = new Date();
    }
    @NotNull
    @Id
    private String token;
    @NotNull
    private Long user_id;
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Date creation;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "consumed_at")
    private Date consumption = null;

    @JsonView(Views.Private.class)
    public Long getUserId() {
        return user_id;
    }

    @JsonView(Views.Private.class)
    public void consumed() {
        this.consumption = new Date();
    }

    @JsonView(Views.Private.class)
    public boolean expired() {
        long now = new Date().getTime();
        long creationTime = creation.getTime();

        // Calcular la diferencia en milisegundos
        long diffInMillis = Math.abs(now - creationTime);

        // Convertir la diferencia a minutos
        long diffInMinutes = diffInMillis / (60 * 1000);
        return consumption != null || diffInMinutes > 10;
    }
}
