package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.PostRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private  UserService userService;


    public Post getPost(Long id) {
        Optional<Post> uo = postRepository.findById(id);
        if (uo.isPresent())
            return uo.get();
        else
            throw new ServiceException(String.format("No existeix Post amb id = %d", id));
    }

    @Transactional
    public void addPost(Long userId, String titol, String descripcio, Double preu) {
        User user = userService.getUser(userId);
        Post post = new Post(titol,descripcio,preu, user);
        user.addPost(post);
        postRepository.save(post);
    }

    public List<Post> getPosts(){
        List<Post> pu = postRepository.findAll();
        if (!pu.isEmpty())
            return pu;
        else
            throw new ServiceException("No hi ha posts");
    }

    public Collection<Post> getPostsUser(Long usuariId){
        User creador = userService.getUser(usuariId);
        Collection<Post> posts = creador.getOwneddPosts();
        if (!posts.isEmpty())
            return posts;
        else
            throw new ServiceException("No hi ha posts");

    }
    public User getUsuari(Long postId){
        return getPost(postId).getUser();
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        if(getPost(postId).getUser().getId()==userId){
            postRepository.deleteById(postId);
        }
        else throw new ServiceException("No es pot eliminar el post");
    }




}
