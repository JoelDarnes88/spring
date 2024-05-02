package org.udg.pds.springtodo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.Task;
import org.udg.pds.springtodo.entity.Token;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.TokenRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.*;

import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    private JavaMailSender mailSender;

    public User matchPassword(String username, String password) {

        List<User> uc = userRepository.findByUsername(username);

        if (uc.size() == 0) throw new ServiceException("User does not exists");

        User u = uc.get(0);

        if (u.getPassword().equals(password))
            return u;
        else
            throw new ServiceException("Password does not match");
    }

    public User register(String username, String name, String country, String email, String phone_number, String password) {

        List<User> uEmail = userRepository.findByEmail(email);
        if (uEmail.size() > 0)
            throw new ServiceException("Email already exist");


        List<User> uUsername = userRepository.findByUsername(username);
        if (uUsername.size() > 0)
            throw new ServiceException("Username already exists");

        User nu = new User(username, name, country, email, phone_number, password);
        userRepository.save(nu);
        return nu;
    }

    public User forgotPassword(String email){
        email = email.replace("\"", "");
        List<User> uEmail = userRepository.findByEmail(email);
        if (uEmail.isEmpty())
            throw new ServiceException("Account doesn't exist");
        if (uEmail.size() > 1)
            throw new ServiceException("More than one account with this email");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("jmassalle.udg@gmail.com");
        mailSender.setPassword("vyiavldlbjeorfzn");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jmasasalle.udg@gmail.com");
        message.setTo(email);
        UUID uuid = UUID.randomUUID();
        String uuidAsString = uuid.toString();
        Token token = new Token(uuidAsString, uEmail.stream().findFirst().get().getId());
        tokenRepository.save(token);
        message.setText("Entri en aquest enllaç per canviar la seva contrasenya: http://localhost:8080/forgotPasswordWeb?token=" + uuidAsString + "\nSàpiga que té 10 min per fer el canvi, després aquest enllaç haurà expirat.");
        message.setSubject("Sol·licitut de canvi de contrasenya");

        mailSender.send(message);

        System.out.println("Mail Sent succesfully");
        return null;
    }

    public User modify(Long userId, String username, String name, String country, String email, String phone_number, String password, String aboutMe) {
        User nu = getUser(userId);
        if(!username.isBlank()) nu.setUsername(username);
        if(!name.isEmpty()) nu.setName(name);
        if(!country.isEmpty()) nu.setCountry(country);
        if(!email.isEmpty()) nu.setEmail(email);
        if(!phone_number.isEmpty()) nu.setPhoneNumber(phone_number);
        if(!password.isEmpty()) nu.setPassword(password);
        if(!aboutMe.isEmpty()) nu.setAboutMe(aboutMe);
        userRepository.save(nu);
        return nu;
    }

    public List<User> searchUser(String query) {
        List<User> users;
        if (query.startsWith("@")) users = userRepository.findSimilarUsername(query.substring(1));
        else users = userRepository.findSimilarName(query);
        return users;
    }

    public User getUser(Long id) {
        Optional<User> uo = userRepository.findById(id);
        if (uo.isPresent())
            return uo.get();
        else
            throw new ServiceException(String.format("User with id = % does not exists", id));
    }

    public User getUserProfile(long id) {
        User u = this.getUser(id);
        for (Task t : u.getTasks())
            t.getTags();
        return u;
    }

    public void deleteUser(Long userId) {
        User u = this.getUser(userId);
        userRepository.delete(u);
    }

    public Collection<Post> getOwnedPosts (Long userId) {
        User u = this.getUser(userId);
        return u.getOwneddPosts();
    }
}
