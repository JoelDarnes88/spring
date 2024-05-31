package org.udg.pds.springtodo.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.Token;
import org.udg.pds.springtodo.entity.User;
import org.udg.pds.springtodo.repository.PostRepository;
import org.udg.pds.springtodo.repository.TokenRepository;
import org.udg.pds.springtodo.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.mail.SimpleMailMessage;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TokenRepository tokenRepository;

    public User matchPassword(String username, String password) {
        List<User> uc = userRepository.findByUsername(username);

        if (uc.size() == 0) throw new ServiceException("User does not exist");

        User u = uc.get(0);

        if (u.getPassword().equals(password))
            return u;
        else
            throw new ServiceException("Password does not match");
    }

    public User register(String username, String name, String country, String email, String phone_number, String password) {
        List<User> uEmail = userRepository.findByEmail(email);
        if (uEmail.size() > 0)
            throw new ServiceException("Email already exists");

        List<User> uUsername = userRepository.findByUsername(username);
        if (uUsername.size() > 0)
            throw new ServiceException("Username already exists");

        User nu = new User(username, name, country, email, phone_number, password, "Visa");
        userRepository.save(nu);
        return nu;
    }

    public User forgotPassword(String email) {
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

        System.out.println("Mail Sent successfully");
        return null;
    }

    public String getPaymentMethod(Long userId) {
        User u = getUser(userId);
        return u.getPaymentMethod();
    }

    public User modify(Long userId, String username, String name, String country, String email, String phone_number, String password, String aboutMe, String paymentMethod) {
        User nu = getUser(userId);
        if (!username.isBlank()) nu.setUsername(username);
        if (!name.isEmpty()) nu.setName(name);
        if (!country.isEmpty()) nu.setCountry(country);
        if (!email.isEmpty()) nu.setEmail(email);
        if (!phone_number.isEmpty()) nu.setPhoneNumber(phone_number);
        if (!password.isEmpty()) nu.setPassword(password);
        if (!aboutMe.isEmpty()) nu.setAboutMe(aboutMe);
        if (!paymentMethod.isEmpty()) nu.setPaymentMethod(paymentMethod);
        userRepository.save(nu);
        return nu;
    }

    public void addToFavorites(Long userId, Long postId) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ServiceException("Post not found"));
        if (!user.getFavorites().contains(post)) {
            user.addToFavorites(post);
            userRepository.save(user);
        }
    }

    public void removeFromFavorites(Long userId, Long postId) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ServiceException("Post not found"));
        if (user.getFavorites().contains(post)) {
            user.removeToFavorites(post);
            userRepository.save(user);
        }
    }

    public boolean isFavorite(Long userId, Long postId) {
        User user = getUser(userId);
        Post post = postRepository.findById(postId).orElseThrow(() -> new ServiceException("Post not found"));
        return user.getFavorites().contains(post);
    }

    public List<Post> getFavoritePosts(Long userId) {
        User user = getUser(userId);
        List<Post> favoritePosts = user.getFavorites();
        // Asegurarse de que los posts tienen los datos necesarios
        favoritePosts.forEach(post -> {
            if (post.getTitol() == null) {
                throw new ServiceException("Post without title detected!");
            }
        });
        return favoritePosts;
    }

    public List<User> searchUser(String query) {
        List<User> users;
        if (query.startsWith("@")) users = userRepository.findByUsernameContainingIgnoreCase(query.substring(1));
        else users = userRepository.findByNameContainingIgnoreCase(query);
        return users;
    }

    public User getUser(Long id) {
        Optional<User> uo = userRepository.findById(id);
        if (uo.isPresent())
            return uo.get();
        else
            throw new ServiceException(String.format("User with id = %d does not exist", id));
    }

    public void deleteUser(Long userId) {
        User u = this.getUser(userId);
        userRepository.delete(u);
    }

    public List<Post> getOwnedPosts(Long userId) {
        User u = this.getUser(userId);
        return u.getOwneddPosts();
    }
}
