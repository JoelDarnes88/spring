package org.udg.pds.springtodo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.FavoritePost;
import org.udg.pds.springtodo.service.FavoritePostService;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoritePostController {

    @Autowired
    private FavoritePostService favoritePostService;

    @GetMapping("/user/{userId}")
    public List<FavoritePost> getFavoritePostsByUserId(@PathVariable Long userId) {
        System.out.println("Fetching favorite posts for user: " + userId);
        List<FavoritePost> favorites = favoritePostService.getFavoritePostsByUserId(userId);
        System.out.println("Fetched favorites: " + favorites.size());
        return favorites;
    }
}
