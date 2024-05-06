package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Post implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    /**  private static final long serialVersionUID = 1L; */

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull
    private String titol;

    @Setter
    @NotNull
    private Double preu;

    @NotNull
    private String descripcio;

    @JsonIgnore
    @ManyToOne
    private User creador;

    @JsonIgnore
    @ManyToOne
    private Servei tipusServei;

    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostImage> images;

    @JsonIgnore
    @ManyToMany(
        fetch = FetchType.LAZY,
        mappedBy = "favorite_posts")
    private List<User> favorited_by;

    public Post() {
    }

    public Post(String titol, String descripcio, Double preu, User creador, Servei tipusServei) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.preu = preu;
        this.creador = creador;
        this.tipusServei = tipusServei;
    }


    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Public.class)
    public String getTitol() {
        return titol;
    }

    @JsonView(Views.Public.class)
    public String getDescripcio() {
        return descripcio;
    }

    @JsonView(Views.Public.class)
    public Double getPreu() {
        return preu;
    }

    @JsonView(Views.Public.class)
    public User getUser() {
        return creador;
    }

    @JsonView(Views.Public.class)
    public Servei getServei() {
        return tipusServei;
    }

    @JsonView(Views.Public.class)
    public List<String> getImages() {
        List<String> image_list = new ArrayList();
        if(images != null)
            for(PostImage image : images) {
                image_list.add(image.getUrl());
            }
        return image_list;
    }

    public void setUser(User creador) {
        this.creador = creador;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public void setServei(Servei tipusServei) {
        this.tipusServei = tipusServei;
    }

    public void setImages(List<PostImage> images) {
        this.images = images;
    }

    @JsonIgnore
    public List<User> getFavoritedBy() {
        return favorited_by;
    }
}
