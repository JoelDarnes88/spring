package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))
public class User implements Serializable {
    /**
     * Default value included to remove warning. Remove or modify at will. *
     */
    private static final long serialVersionUID = 1L;

    public User() {
    }

    public User(String username, String name, String country, String email, String phone_number, String password) {
        this.username = username;
        this.name = name;
        this.country = country;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.about_me = "";
        this.wallet = 0.0;
        this.tasks = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String name;

    @NotNull
    private String country;

    @NotNull
    private String email;

    @NotNull
    private String phone_number;

    @NotNull
    private String password;

    @NotNull
    private String about_me;

    @NotNull
    private Double wallet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private Collection<Task> tasks;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creador")
    private Collection<Post> posts;

    @JsonView(Views.Private.class)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonView(Views.Public.class)
    public String getUsername() {
        return username;
    }

    @JsonView(Views.Public.class)
    public String getName() {
        return name;
    }

    @JsonView(Views.Public.class)
    public String getCountry() {
        return country;
    }

    @JsonView(Views.Private.class)
    public String getEmail() {
        return email;
    }

    @JsonView(Views.Private.class)
    public String getPhoneNumber() {
        return phone_number;
    }

    @JsonView(Views.Private.class)
    public double getWallet() {
        return wallet;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonView(Views.Complete.class)
    public Collection<Task> getTasks() {
        // Since tasks is collection controlled by JPA, it has LAZY loading by default. That means
        // that you have to query the object (calling size(), for example) to get the list initialized
        // More: http://www.javabeat.net/jpa-lazy-eager-loading/
        tasks.size();
        return tasks;
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addPost(Post post) { posts.add(post); }

    @JsonView(Views.Complete.class)
    public Collection<Post> getOwneddPosts() {
        posts.size();
        return posts;
    }
}
