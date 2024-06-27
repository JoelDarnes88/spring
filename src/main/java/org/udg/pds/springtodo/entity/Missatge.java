package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "missatges")
public class Missatge implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String missatge;

    @NotNull
    private LocalDateTime dataEnviament;

    @NotNull
    private Boolean propietari;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solicitud_id")
    private Solicitud solicitud;

    public Missatge() {
    }

    public Missatge(String missatge, LocalDateTime dataEnviament, Boolean propietari, Solicitud solicitud) {
        this.missatge = missatge;
        this.dataEnviament = dataEnviament;
        this.propietari = propietari;
        this.solicitud = solicitud;
    }

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Public.class)
    public String getMissatge() {
        return missatge;
    }

    public void setMissatge(String missatge) {
        this.missatge = missatge;
    }

    @JsonView(Views.Public.class)
    public LocalDateTime getDataEnviament() {
        return dataEnviament;
    }

    public void setDataEnviament(LocalDateTime dataEnviament) {
        this.dataEnviament = dataEnviament;
    }

    @JsonView(Views.Public.class)
    public Boolean getPropietari() {
        return propietari;
    }

    public void setPropietari(Boolean propietari) {
        this.propietari = propietari;
    }

    @JsonView(Views.Public.class)
    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }

}
