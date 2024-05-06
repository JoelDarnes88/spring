package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "posts")
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
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

    @NotNull
    private Long userId;

    @JsonIgnore
    @ManyToOne
    private User creador;

    @OneToMany(mappedBy = "post_id", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Collection<PostImage> images;


    public Post() {
    }

    @ManyToMany(mappedBy = "favorite_posts")
    private List<User> favorited_by;

    public Post(String titol, String descripcio, Double preu, User creador) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.preu = preu;
        this.creador = creador;
        this.userId = creador.getId();
    }


    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Public.class)
    public Long getUserId() {
        return userId;
    }

    @JsonView(Views.Public.class)
    public String getTitol() {
        return titol;
    }

    @JsonView(Views.Public.class)
    public User getUser() {
        return creador;
    }

    public void setUser(User creador) {this.creador = creador;}
    public void setDescripcio(String descripcio) {this.descripcio = descripcio;}

    @JsonView(Views.Public.class)
    public Double getPreu() {return preu;}

    @JsonView(Views.Public.class)
    public String getDescripcio() {return descripcio;}

    @JsonView(Views.Public.class)
    public List<String> getImages() {
        List<String> image_list = new ArrayList();
        if(images != null)
            for(PostImage image : images) {
                image_list.add(image.getUrl());
            }
        return image_list;
    }

    @JsonIgnore
    public List<User> getFavoritedBy() {
        return favorited_by;
    }

    public void setImages(Collection<PostImage> images) {
        this.images = images;
    }
}
