package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.configuration.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping(path="/users")
@RestController
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    PostService postService;

    @PostMapping(path="/login")
    @JsonView(Views.Private.class)
    public User login(HttpSession session, @Valid @RequestBody LoginUser user) {
        checkNotLoggedIn(session);

        User u = userService.matchPassword(user.username, user.password);
        session.setAttribute("simpleapp_auth_id", u.getId());
        return u;
    }

    @PostMapping(path="/logout")
    @JsonView(Views.Private.class)
    public String logout(HttpSession session) {

        getLoggedUser(session);

        session.removeAttribute("simpleapp_auth_id");
        return BaseController.OK_MESSAGE;
    }

    @GetMapping("/search")
    @JsonView(Views.Public.class)
    public List<User> searchUser(HttpSession session, @RequestParam("query") String query) {

        getLoggedUser(session);

        return userService.searchUser(query);
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public User getPublicUser(HttpSession session, @PathVariable("id") @Valid Long userId) {

        getLoggedUser(session);

        return userService.getUser(userId);
    }

    @DeleteMapping(path="/{id}")
    public String deleteUser(HttpSession session, @PathVariable("id") Long userId) {

        Long loggedUserId = getLoggedUser(session);

        if (!loggedUserId.equals(userId))
            throw new ControllerException("You cannot delete other users!");

        userService.deleteUser(userId);
        session.removeAttribute("simpleapp_auth_id");

        return BaseController.OK_MESSAGE;
    }

    @PostMapping(path="/register", consumes = "application/json")
    public String register(HttpSession session, @Valid  @RequestBody RegisterUser ru) {
        checkNotLoggedIn(session);
        userService.register(ru.username, ru.name, ru.country, ru.email, ru.phone_number, ru.password);
        return BaseController.OK_MESSAGE;
    }

    @PostMapping(path="/forgotPassword", consumes = "application/json")
    public String forgotPassword(HttpSession session, @Valid @RequestBody String email) {
        checkNotLoggedIn(session);
        userService.forgotPassword(email);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping(path="/getPaymentMethod")
    public Map<String, String> getPaymentMethod(HttpSession session) {
        Long userId = getLoggedUser(session);
        String paymentMethod = userService.getPaymentMethod(userId);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("paymentMethod", paymentMethod);

        return resultMap;
    }

    @PostMapping(path="/modify", consumes = "application/json")
    public String modify(HttpSession session, @Valid  @RequestBody ModifyUser mu) {
        Long userId = getLoggedUser(session);
        userService.modify(userId, mu.username, mu.name, mu.country, mu.email, mu.phone_number, mu.password, mu.about_me, mu.payment_method);
        return BaseController.OK_MESSAGE;
    }

    @PostMapping("/changeFavourites/{post_id}")
    public ResponseEntity<String> changeFavourites(HttpSession session, @PathVariable("post_id") Long postId, @RequestBody Boolean addPost) {
        Long userId = getLoggedUser(session);
        if (addPost) {
            userService.addToFavorites(userId, postId);
        } else {
            userService.removeFromFavorites(userId, postId);
        }
        return ResponseEntity.ok("Favorite status changed");
    }

    @GetMapping("/favorites/user/{userId}")
    @JsonView(Views.Public.class)
    public ResponseEntity<?> getFavoritePosts(HttpSession session, @PathVariable("userId") Long userId) {
        Long loggedUserId = getLoggedUser(session);  // Ensure user is logged in
        if (!loggedUserId.equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        try {
            List<Post> favoritePosts = userService.getFavoritePosts(userId);
            return ResponseEntity.ok(favoritePosts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/isFavourite/{post_id}")
    public ResponseEntity<Boolean> isFavourite(HttpSession session, @PathVariable("post_id") Long postId) {
        Long userId = getLoggedUser(session);
        boolean isFavourite = userService.isFavorite(userId, postId);
        return ResponseEntity.ok(isFavourite);
    }

    @GetMapping(path="/check")
    public String checkLoggedIn(HttpSession session) {

        getLoggedUser(session);

        return BaseController.OK_MESSAGE;
    }

    private static class LoginUser {
        @NotNull
        public String username;
        @NotNull
        public String password;
    }

    private static class RegisterUser {
        @NotNull
        public String username;
        @NotNull
        public String name;
        @NotNull
        public String country;
        @NotNull
        public String email;
        @NotNull
        public String phone_number;
        @NotNull
        public String password;
    }

    private static class ModifyUser {
        @NotNull
        public String username;
        @NotNull
        public String name;
        @NotNull
        public String country;
        @NotNull
        public String email;
        @NotNull
        public String phone_number;
        @NotNull
        public String password;
        @NotNull
        public String about_me;
        @NotNull
        public String payment_method;
    }
}
