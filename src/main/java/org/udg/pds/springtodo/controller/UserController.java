package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import kotlin.text.UStringsKt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.configuration.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.entity.Views;
import org.udg.pds.springtodo.service.UserService;

import java.util.Collection;
import java.util.List;

// This class is used to process all the authentication related URLs
@RequestMapping(path="/users")
@RestController
public class UserController extends BaseController {

  @Autowired
  UserService userService;

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

  @PostMapping(path="/modify", consumes = "application/json")
  public String modify(HttpSession session, @Valid  @RequestBody ModifyUser ru) {
      Long userId = getLoggedUser(session);
      userService.modify(userId, ru.username, ru.name, ru.country, ru.email, ru.phone_number, ru.password, ru.about_me);
      return BaseController.OK_MESSAGE;
  }

  @GetMapping(path="/me")
  @JsonView(Views.Complete.class)
  public User getUserProfile(HttpSession session) {

    Long loggedUserId = getLoggedUser(session);

    return userService.getUserProfile(loggedUserId);
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
  }

}
