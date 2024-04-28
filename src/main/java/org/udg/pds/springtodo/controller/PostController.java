package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.DTO.PostBasicDTO;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.PostService;

import java.util.Collection;
import java.util.List;

@RequestMapping(path="/posts")
@RestController
public class PostController extends BaseController {

    @Autowired
    PostService postService;



    @GetMapping(path="/me")
    @JsonView(Views.Public.class)
    public Collection<Post> getMeusPosts(HttpSession session){
        Long id = getLoggedUser(session);
        return postService.getPostsUser(id);
    }
    @GetMapping(path="/user/{userId}")
    @JsonView(Views.Public.class)
    public Collection<Post> getPostsUser(HttpSession session, @PathVariable("userId") String userIdstr){
        getLoggedUser(session);
        Long userId = Long.parseLong(userIdstr);
        return postService.getPostsUser(userId);
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public Post getPost(HttpSession session, @PathVariable("id") Long id) {
        getLoggedUser(session);
        return postService.getPost(id);
    }


    @PostMapping(path="/post", consumes = "application/json")
    public String addPost(HttpSession session, @Valid @RequestBody PostRequest postRequest) {
        Long id = getLoggedUser(session);
        postService.addPost(id, postRequest.titol, postRequest.descripcio, postRequest.preu);
        return BaseController.OK_MESSAGE;
    }


    @DeleteMapping(path="/{id}")
    public String deletePost(HttpSession session, @PathVariable("id") Long postId){
        Long loggedUserId = getLoggedUser(session);
        postService.deletePost(postId, loggedUserId);
        return BaseController.OK_MESSAGE;
    }


    @GetMapping(path="")
    @JsonView(Views.Public.class)
    public List<Post> getPosts(HttpSession session){
        getLoggedUser(session);
        return postService.getPosts();
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public String updatePost(HttpSession session,@PathVariable("id") Long postId, @Valid @RequestBody PostRequest postRequest) throws Exception {
        Long id = getLoggedUser(session);
        postService.updatePost(id, postId, postRequest.titol, postRequest.descripcio, postRequest.preu);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostBasicDTO>> searchPosts(@RequestParam String query) {
        List<PostBasicDTO> posts = postService.searchByTitol(query);
        return ResponseEntity.ok(posts);
    }



    private static class PostRequest {
        @NotNull
        public String titol;
        @NotNull
        public String descripcio;
        @NotNull
        public Double preu;
    }

}

