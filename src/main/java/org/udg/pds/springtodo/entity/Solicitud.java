package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "solicituds")
public class Solicitud implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuari_id")
    private User usuari;

    @NotNull
    private String estat;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "solicitud")
    private Collection<Missatge> missatges = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pagament_id", referencedColumnName = "id")
    private Pagament pagament;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Solicitud() {
    }

    public Solicitud(User usuari, String estat, Post post) {
        this.usuari = usuari;
        this.estat = estat;
        this.post = post;
    }

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Private.class)
    public String getEstat() {
        return estat;
    }

    public void setEstat(String estat) {
        this.estat = estat;
    }

    @JsonView(Views.Public.class)
    public Collection<Missatge> getMissatges() {
        return missatges;
    }

    public void addMissatge(Missatge missatge) {
        missatges.add(missatge);
        missatge.setSolicitud(this);
    }

    @JsonView(Views.Private.class)
    public User getUsuari() {
        return usuari;
    }

    public void setUsuari(User usuari) {
        this.usuari = usuari;
    }

    @JsonView(Views.Private.class)
    public Pagament getPagament() {
        return pagament;
    }

    public void setPagament(Pagament pagament) {
        this.pagament = pagament;
    }

    @JsonView(Views.Private.class)
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
