package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.Servei;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.ServeiService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequestMapping(path="/services")
@RestController
public class ServeiController extends BaseController {

    @Autowired
    ServeiService serveiService;

    @Autowired
    PostService postService;

    @GetMapping(path="")
    @JsonView(Views.Public.class)
    public List<Servei> getServices(HttpSession session){
        getLoggedUser(session);
        return serveiService.getServices();
    }

    @GetMapping(path="/user/{userId}")
    @JsonView(Views.Public.class)
    public List<Servei> getServicesUser(HttpSession session, @PathVariable("userId") String userIdstr){
        getLoggedUser(session);
        Long userId = Long.parseLong(userIdstr);
        List<Post> posts = postService.getUserPosts(userId);

        Set<Servei> uniqueServices = new HashSet<>();
        for (Post post : posts) {
            uniqueServices.add(post.getServei());
        }
        return new ArrayList<>(uniqueServices);
    }
}
