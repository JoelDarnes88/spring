package org.udg.pds.springtodo.DTO;

import org.udg.pds.springtodo.entity.Post;

public class PostBasicDTO {
    private Long id;
    private String titol;
    private String descripcio;
    private Double preu;


    public static PostBasicDTO fromEntity(Post post) {
        PostBasicDTO dto = new PostBasicDTO();
        dto.setId(post.getId());
        dto.setTitol(post.getTitol());
        dto.setDescripcio(post.getDescripcio());
        dto.setPreu(post.getPreu());
        return dto;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitol() {
        return titol;
    }

    public void setTitol(String titol) {
        this.titol = titol;
    }

    public String getDescripcio() {
        return descripcio;
    }

    public void setDescripcio(String descripcio) {
        this.descripcio = descripcio;
    }

    public Double getPreu() {
        return preu;
    }

    public void setPreu(Double preu) {
        this.preu = preu;
    }
}
