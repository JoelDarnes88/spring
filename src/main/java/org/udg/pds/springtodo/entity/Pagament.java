package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "pagaments")
public class Pagament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Double preuFinal;

    @NotNull
    private Integer metode;

    @OneToOne(mappedBy = "pagament")
    private Solicitud solicitud;

    public Pagament() {
    }

    public Pagament(Double preuFinal, Integer metode) {
        this.preuFinal = preuFinal;
        this.metode = metode;
    }

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Private.class)
    public Double getPreuFinal() {
        return preuFinal;
    }

    public void setPreuFinal(Double preuFinal) {
        this.preuFinal = preuFinal;
    }

    @JsonView(Views.Private.class)
    public Integer getMetode() {
        return metode;
    }

    public void setMetode(Integer metode) {
        this.metode = metode;
    }

    @JsonView(Views.Complete.class)
    public Solicitud getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(Solicitud solicitud) {
        this.solicitud = solicitud;
    }
}
