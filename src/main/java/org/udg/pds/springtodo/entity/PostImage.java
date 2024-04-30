package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity(name = "PostImage")
@Table(name = "post_images")
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String url;
    private Long post_id;

    public PostImage() {

    }
    public PostImage(Long post_id, String url) {
        this.post_id = post_id;
        this.url = url;
    }

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
    }

    @JsonView(Views.Public.class)
    public String getUrl() {
        return url;
    }

    @JsonView(Views.Public.class)
    public Long getPostId() {
        return post_id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public void setPostId(Long product) {
        this.post_id = post_id;
    }
}
