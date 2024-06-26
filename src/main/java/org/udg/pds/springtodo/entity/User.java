package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "users")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"email", "username"}))

public class User implements Serializable {
    private static final long serialVersionUID = 1L;

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
    private String payment_method;

    @NotNull
    private Double wallet;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creador")
    @JsonIgnore  // Evitar referencia circular
    private List<Post> posts;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "favorites",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "post_id")
    )
    private List<Post> favorite_posts;

    public User() {
    }

    public User(String username, String name, String country, String email, String phone_number, String password, String payment_method) {
        this.username = username;
        this.name = name;
        this.country = country;
        this.email = email;
        this.phone_number = phone_number;
        this.password = password;
        this.payment_method = payment_method;
        this.about_me = "";
        this.wallet = 0.0;
    }

    @JsonView(Views.Public.class)
    public Long getId() {
        return id;
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

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    public String getPaymentMethod() {
        return payment_method;
    }

    @JsonView(Views.Public.class)
    public String getAbout_me() {
        return about_me;
    }

    @JsonView(Views.Private.class)
    public double getWallet() {
        return wallet;
    }

    @JsonView(Views.Private.class)
    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public List<Post> getFavorites() {
        return favorite_posts;
    }

    @JsonView(Views.Private.class)
    public void setUsername(String username) {
        this.username = username;
    }

    @JsonView(Views.Private.class)
    public void setName(String name) {
        this.name = name;
    }

    @JsonView(Views.Private.class)
    public void setCountry(String country) {
        this.country = country;
    }

    @JsonView(Views.Private.class)
    public void setEmail(String email) {
        this.email = email;
    }

    @JsonView(Views.Private.class)
    public void setPhoneNumber(String phone_number) {
        this.phone_number = phone_number;
    }

    @JsonView(Views.Private.class)
    public void setWallet(double wallet) {
        this.wallet = wallet;
    }

    @JsonView(Views.Private.class)
    public void setPassword(String password) {
        this.password = password;
    }

    @JsonView(Views.Private.class)
    public void setAboutMe(String about_me) {
        this.about_me = about_me;
    }

    @JsonView(Views.Private.class)
    public void setPaymentMethod(String payment_method) {
        this.payment_method = payment_method;
    }

    public void addPost(Post post) {
        posts.add(post);
    }

    @JsonView(Views.Complete.class)
    public List<Post> getOwneddPosts() {
        posts.size();
        return posts;
    }

    public void addToFavorites(Post post) {
        favorite_posts.add(post);
    }

    public void removeToFavorites(Post post) {
        favorite_posts.remove(post);
    }
}
