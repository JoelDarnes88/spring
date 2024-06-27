package org.udg.pds.springtodo;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.configuration.exceptions.ServiceException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.ChatService;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.ServeiService;
import org.udg.pds.springtodo.service.UserService;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class Global {
    public static final DateTimeFormatter AppDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy - HH:mm:ss z");

    @Getter
    private MinioClient minioClient;

    private final Logger logger = LoggerFactory.getLogger(Global.class);

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ServeiService serveiService;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${todospring.minio.url:}")
    private String minioURL;

    @Value("${todospring.minio.access-key:}")
    private String minioAccessKey;

    @Value("${todospring.minio.secret-key:}")
    private String minioSecretKey;

    @Getter
    @Value("${todospring.minio.bucket:}")
    private String minioBucket;

    @Value("${todospring.base-url:#{null}}")
    private String BASE_URL;

    @Value("${todospring.base-port:8080}")
    private String BASE_PORT;

    @PostConstruct
    void init() {
        logger.info(String.format("Starting Minio connection to URL: %s", minioURL));
        try {
            minioClient = MinioClient.builder()
                .endpoint(minioURL)
                .credentials(minioAccessKey, minioSecretKey)
                .build();
        } catch (Exception e) {
            logger.warn("Cannot initialize minio service with url:" + minioURL + ", access-key:" + minioAccessKey + ", secret-key:" + minioSecretKey);
        }

        if (minioBucket.equals("")) {
            logger.warn("Cannot initialize minio bucket: " + minioBucket);
            minioClient = null;
        }

        if (BASE_URL == null) BASE_URL = "http://localhost";
        BASE_URL += ":" + BASE_PORT;

        initData();
    }

    private void initData() {
        if (activeProfile.equals("dev")) {
            logger.info("Starting populating database ...");

            List<Servei> serveis = new ArrayList<>();
            serveis.add(serveiService.addService("Pintura", "Experiència professional per aplicar pintura a superfícies i proporcionar recobriments protectors."));
            serveis.add(serveiService.addService("Fusteria", "Elaborar i donar forma a la fusta en diversos elements funcionals o decoratius."));
            serveis.add(serveiService.addService("Cosir", "Cosir teixits per crear o reparar peces de vestir, tèxtils o altres articles."));
            serveis.add(serveiService.addService("Informatica", "Assistència i solucions per a problemes tècnics relacionats amb sistemes informàtics."));

            User user1 = registerUserIfNotExists("usuari", "Carles M.", "United Kingdom", "usuari@hotmail.com", "+44 123456789", "123456");
            User user2 = registerUserIfNotExists("user", "Monica G.", "Spain", "user@hotmail.com", "+34 123456789", "0000");
            User user3 = registerUserIfNotExists("a", "Josep A.", "Spain", "usera@hotmail.com", "+34 123456789", "a");

            if (user1 != null) {
                postService.addPost(user1.getId(), "Pintura de murs", "Pintaré qualsevol mur que tinguis", 25.5, serveis.get(0));
                postService.addPost(user1.getId(), "Pintura de mobles", "Pintaré mobles i aplicaré una capa de protecció", 17.0, serveis.get(0));
                postService.addPost(user1.getId(), "Casa per ocells", "Construiré una casa d'ocells de 50x50cm", 49.5, serveis.get(1));
                Collection<Post> posts = userService.getOwnedPosts(user1.getId());
                this.addProductsImages(posts);
            }

            if (user2 != null) {
                Post p1 = postService.addPost(user2.getId(), "Cosir un jersei", "Cossiré un jersei del color que vulguis", 38.0, serveis.get(2));
                postService.addPost(user2.getId(), "Crear compte email", "T'ajudaré a crear un compte email", 5.0, serveis.get(3));
                Collection<Post> posts = userService.getOwnedPosts(user2.getId());
                this.addProductsImages(posts);

                // CHATS HARDCODED:
                Chat chat1 = chatService.createChat(user1, user2, p1);
                chatService.sendMessageDTO(chat1, user1, "Hola, ¿que tal?");
                chatService.sendMessageDTO(chat1, user2, "Be, gracies i tu");
            }

            if (user3 != null) {
                Post p2 = postService.getUserPosts(user3.getId()).iterator().next();  // Obtener el primer post del usuario 3
                Chat chat2 = chatService.createChat(user1, user3, p2);
                chatService.sendMessageDTO(chat2, user1, "HOLA!");
                chatService.sendMessageDTO(chat2, user3, "Hola que tal?");
                chatService.sendMessageDTO(chat2, user1, "Es pot negociar el preu?");
            }
        }
    }

    private User registerUserIfNotExists(String username, String name, String country, String email, String phone, String password) {
        try {
            return userService.register(username, name, country, email, phone, password);
        } catch (ServiceException e) {
            logger.warn("Error al registrar usuario: " + e.getMessage());
            return null;
        }
    }

    private void addProductsImages(Collection<Post> posts) {
        for (Post p : posts) {
            PostImage img1 = new PostImage(p.getId(), "http://localhost:8080/posts/image/1.jpg");
            PostImage img2 = new PostImage(p.getId(), "http://localhost:8080/posts/image/2.jpg");
            PostImage img3 = new PostImage(p.getId(), "http://localhost:8080/posts/image/3.jpg");
            postService.addDefaultImages(Arrays.asList(img1, img2, img3));
        }
    }

    public String getBaseURL() {
        return BASE_URL;
    }
}
