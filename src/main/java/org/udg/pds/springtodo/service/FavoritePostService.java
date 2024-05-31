package org.udg.pds.springtodo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.entity.FavoritePost;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.FavoritePostRepository;

import java.util.List;

@Service
public class FavoritePostService {

    @Autowired
    private FavoritePostRepository favoritePostRepository;

    public List<FavoritePost> getFavoritePostsByUserId(Long userId) {
        System.out.println("Fetching favorite posts for user: " + userId);
        List<FavoritePost> favorites = favoritePostRepository.findByUserId(userId);
        System.out.println("Fetched " + favorites.size() + " favorites");
        return favorites;
    }

    public void addFavorite(User user, Post post) {
        FavoritePost favoritePost = new FavoritePost();
        favoritePost.setUserId(user.getId());
        favoritePost.setPost(post);
        favoritePostRepository.save(favoritePost);
    }
}
