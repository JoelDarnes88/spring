package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

@Entity(name = "servei")
public class Servei implements Serializable {

    public Servei() {
    }

    public Servei(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Public.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Public.class)
    public String getDescription() {
        return description;
    }

    @JsonView(Views.Private.class)
    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Private.class)
    public void setName(String name) {
        this.name = name;
    }

    @JsonView(Views.Private.class)
    public void setDescription(String description) {
        this.description = description;
    }
}
