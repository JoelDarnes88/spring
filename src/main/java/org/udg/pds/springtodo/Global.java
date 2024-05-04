package org.udg.pds.springtodo;

import io.minio.MinioClient;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.service.TagService;
import org.udg.pds.springtodo.service.TaskService;
import org.udg.pds.springtodo.service.UserService;

import java.time.ZonedDateTime;
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
    private TaskService taskService;

    @Autowired
    private TagService tagService;

    @Autowired
    private PostService postService;

    @Autowired
    private Environment environment;

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

            User user = userService.register("usuari", "Carles M.", "United Kingdom", "usuari@hotmail.com", "+44 123456789", "123456");
            User user2 = userService.register("user", "Monica G.", "Spain", "user@hotmail.com", "+34 123456789", "0000");
            IdObject taskId = taskService.addTask("Una tasca", user.getId(), AppDateFormatter.format(ZonedDateTime.now()), AppDateFormatter.format(ZonedDateTime.now()));
            Tag tag = tagService.addTag("ATag", "Just a tag");
            taskService.addTagsToTask(user.getId(), taskId.getId(), new ArrayList<Long>() {{
                add(tag.getId());
            }});

            //POSTS
            postService.addPost(user.getId(),"titol", "descripcio", 25.5);
            postService.addPost(user.getId(),"titol2", "descripcio2", 24.5);
            Collection<Post> p = userService.getOwnedPosts(user.getId());
            this.addProductsImages(p);

            postService.addPost(user2.getId(),"pintor", "pinta", 30.0);
            postService.addPost(user2.getId(),"mecanic", "taller", 50.0);
            Collection<Post> p2 = userService.getOwnedPosts(user2.getId());
            this.addProductsImages(p2);
        }
    }
    public String getBaseURL() {
        return BASE_URL;
    }

    private void addProductsImages(Collection<Post> posts) {
        for(Post p : posts) {
            PostImage img1 = new PostImage(p.getId(), "http://localhost:8080/posts/image/1.jpg");
            PostImage img2 = new PostImage(p.getId(), "http://localhost:8080/posts/image/2.jpg");
            PostImage img3 = new PostImage(p.getId(), "http://localhost:8080/posts/image/3.jpg");
            postService.addDefaultImages(Arrays.asList(img1, img2, img3));
        }
    }
}
