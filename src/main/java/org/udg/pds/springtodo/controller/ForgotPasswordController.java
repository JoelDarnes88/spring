package org.udg.pds.springtodo.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.entity.Token;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.TokenRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.List;

@Controller
public class ForgotPasswordController {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping(path="/forgotPasswordWeb")
    public String forgotPasswordWeb(@RequestParam(name="token", required=false, defaultValue="noToken") String string_token, Model model) {
        List<Token> utoken = tokenRepository.findByToken(string_token);
        if(utoken.isEmpty()) {
            return "passwordChangedBad";
        } else{
            Token token = utoken.stream().findFirst().get();
            if(token.expired()){
                return "forgotPasswordExpired";
            } else{
                model.addAttribute("token", string_token);
                return "forgotPasswordWeb";
            }
        }
    }

    @PostMapping(path="/passwordChanged", consumes = "application/x-www-form-urlencoded")
    public String passwordChanged(@RequestParam("password") String password,
                                  @RequestParam("confirmPassword") String confirmPassword,
                                  @RequestParam("token") String string_token,
                                  Model model) {
        Token token = tokenRepository.findByToken(string_token).stream().findFirst().get();
        Long user_id = token.getUserId();
        User user = userRepository.findById(user_id).stream().findAny().get();
        user.setPassword(password);
        userRepository.save(user);
        model.addAttribute("user", user.getName());
        token.consumed();
        tokenRepository.save(token);
        return "passwordChangedGood";

    }
}
