package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.udg.pds.springtodo.DTO.PostBasicDTO;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.PostImageRepository;
import org.udg.pds.springtodo.repository.PostRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostImageRepository postImageRepository;

    @Autowired
    public PostService(PostRepository postRepository, UserRepository userRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.postImageRepository = postImageRepository;
    }

    public Post getPost(Long id) {
        Optional<Post> uo = postRepository.findById(id);
        if (uo.isPresent())
            return uo.get();
        else
            throw new ServiceException(String.format("No existeix Post amb id = %d", id));
    }

    public List<Post> getPosts() {
        List<Post> pu = postRepository.findAll();
        if (!pu.isEmpty())
            return pu;
        else
            throw new ServiceException("No hi ha posts");
    }

    public List<Post> getUserPosts(Long usuariId) {
        User creador = userRepository.findById(usuariId).orElseThrow(() -> new ServiceException("User not found"));
        List<Post> posts = creador.getOwneddPosts();
        if (!posts.isEmpty())
            return posts;
        else
            throw new ServiceException("No hi ha posts");
    }

    public User getUsuari(Long postId) {
        return getPost(postId).getUser();
    }

    @Transactional
    public Post addPost(Long userId, String titol, String descripcio, Double preu, Servei tipusServei) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ServiceException("User not found"));
        Post post = new Post(titol, descripcio, preu, user, tipusServei);
        user.addPost(post);
        postRepository.save(post);
        return post;
    }

    @Transactional
    public Post updatePost(Long userId, Long postId, String titol, String descripcio, Double preu, Servei tipusServei) throws Exception {
        Post post = postRepository.findById(postId).orElseThrow(() -> new Exception("Post not found"));
        if (!post.getUser().getId().equals(userId)) throw new Exception("Unauthorized to update this post");
        post.setTitol(titol);
        post.setDescripcio(descripcio);
        post.setPreu(preu);
        post.setServei(tipusServei);
        postRepository.save(post);
        return post;
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = getPost(postId);
        if (post.getUser().getId().equals(userId)) {
            deleteImages(post.getImages(), postId);
            postRepository.deleteById(postId);
        } else {
            throw new ServiceException("No es pot eliminar el post");
        }
    }

    @Transactional
    public List<PostBasicDTO> searchPosts(String query) {
        List<Post> posts;
        if (!query.startsWith("#")) posts = postRepository.findByTitolContainingIgnoreCase(query);
        else posts = postRepository.findByTipusServeiNameContainingIgnoreCase(query.substring(1));
        return posts.stream().map(PostBasicDTO::fromEntity).collect(Collectors.toList());
    }

    public List<PostImage> addDefaultImages(List<PostImage> images) {
        return postImageRepository.saveAll(images);
    }

    public Boolean deleteImage(String image) {
        List<PostImage> images = postImageRepository.findAllByUrl(image);
        postImageRepository.deleteAll(images);
        return true;
    }

    public void deleteImages(List<String> urlsToDel, Long postId) {
        for (String url : urlsToDel) {
            String cleanedUrl = url.replace("\"", ""); // netejar urls amb doble cometes x2
            List<PostImage> lImg = postImageRepository.findAllByUrl(cleanedUrl);
            for (PostImage img : lImg) {
                if (img.getPostId().equals(postId)) {
                    postImageRepository.deleteById(img.getId());
                }
            }
        }
    }
}
