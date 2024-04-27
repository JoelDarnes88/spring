package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.io.Serializable;

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




    public Post() {
    }

    public Post(String titol, String descripcio, Double preu, User creador) {
        this.titol = titol;
        this.descripcio = descripcio;
        this.preu = preu;
        this.creador = creador;
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
    public User getUser() {
        return creador;
    }

    public void setUser(User creador) {this.creador = creador;}
    public void setDescripcio(String descripcio) {this.descripcio = descripcio;}

    @JsonView(Views.Public.class)
    public Double getPreu() {return preu;}

    @JsonView(Views.Public.class)
    public String getDescripcio() {return descripcio;}

}
