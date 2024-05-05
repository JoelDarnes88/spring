package org.udg.pds.springtodo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.udg.pds.springtodo.DTO.PostBasicDTO;
import org.springframework.web.multipart.MultipartFile;
import org.udg.pds.springtodo.configuration.exceptions.ControllerException;
import org.udg.pds.springtodo.entity.*;
import org.udg.pds.springtodo.repository.PostImageRepository;
import org.udg.pds.springtodo.service.PostService;
import org.udg.pds.springtodo.Global;

import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequestMapping(path="/posts")
@RestController
public class PostController extends BaseController {

    @Autowired
    PostService postService;

    @Autowired
    private PostImageRepository postImageRepository;

    @Autowired
    Global global;


    @GetMapping(path="/me")
    @JsonView(Views.Public.class)
    public Collection<Post> getMeusPosts(HttpSession session){
        Long id = getLoggedUser(session);
        return postService.getPostsUser(id);
    }
    @GetMapping(path="/user/{userId}")
    @JsonView(Views.Public.class)
    public Collection<Post> getPostsUser(HttpSession session, @PathVariable("userId") String userIdstr){
        getLoggedUser(session);
        Long userId = Long.parseLong(userIdstr);
        return postService.getPostsUser(userId);
    }

    @GetMapping(path="/{id}")
    @JsonView(Views.Public.class)
    public Post getPost(HttpSession session, @PathVariable("id") Long id) {
        getLoggedUser(session);
        return postService.getPost(id);
    }


    @PostMapping(path="/post", consumes = "application/json")
    public ResponseEntity<Long> addPost(HttpSession session, @Valid @RequestBody PostRequest postRequest) {
        Long id = getLoggedUser(session);
        Post post = postService.addPost(id, postRequest.titol, postRequest.descripcio, postRequest.preu);
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }

    @PutMapping("/updatePostImage/{postId}")
    public ResponseEntity<?> updatePostImage(HttpSession session,
                                             @PathVariable Long postId,
                                             @RequestParam("titol") String titol,
                                             @RequestParam("descripcio") String descripcio,
                                             @RequestParam("preu") Double preu,
                                             @RequestParam(required = false) List<String> urlsToDel,
                                             @RequestParam(required = false) List<MultipartFile> files) throws Exception {

        Long id = getLoggedUser(session);
        if(urlsToDel != null) postService.deleteImages(urlsToDel, postId);

        Post post = postService.updatePost(id, postId, titol, descripcio, preu);

        if(files != null){
            MinioClient minioClient = global.getMinioClient();
            if (minioClient == null)
                throw new ControllerException("Minio client not configured");
            try {
                for (MultipartFile file : files) {
                    InputStream istream = file.getInputStream();
                    String contentType = file.getContentType();
                    UUID imgName = UUID.randomUUID();
                    String objectName = imgName + "." + FilenameUtils.getExtension(file.getOriginalFilename());

                    minioClient.putObject(
                        PutObjectArgs.builder()
                            .bucket(global.getMinioBucket())
                            .object(objectName)
                            .stream(istream, -1, 10485760)
                            .build());

                    PostImage postImg = new PostImage(post.getId(), "http://localhost:8080/posts/image/" + objectName);
                    postImageRepository.save(postImg);
                }
            }
            catch (Exception e) {
                throw new ControllerException("Error saving file: " + e.getMessage());
            }
        }
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }


    @PostMapping(path="/postImage", consumes = "multipart/form-data")
    public ResponseEntity<Long> addPostImage(HttpSession session,
                                             @RequestParam("titol") String titol,
                                             @RequestParam("descripcio") String descripcio,
                                             @RequestParam("preu") Double preu,
                                             @RequestParam("files") List<MultipartFile> files) {
        Long id = getLoggedUser(session);
        Post post = postService.addPost(id, titol, descripcio, preu);

        MinioClient minioClient = global.getMinioClient();
        if (minioClient == null)
            throw new ControllerException("Minio client not configured");
        try {
            for(MultipartFile file : files) {
                InputStream istream = file.getInputStream();
                String contentType = file.getContentType();
                UUID imgName = UUID.randomUUID();
                String objectName = imgName + "." + FilenameUtils.getExtension(file.getOriginalFilename());

                minioClient.putObject(
                    PutObjectArgs.builder()
                        .bucket(global.getMinioBucket())
                        .object(objectName)
                        .stream(istream, -1, 10485760)
                        .build());

                PostImage postImg = new PostImage(post.getId(), "http://localhost:8080/posts/image/" + objectName);
                postImageRepository.save(postImg);
            }
        }
        catch (Exception e) {
            throw new ControllerException("Error saving file: " + e.getMessage());
        }
        return new ResponseEntity<>(post.getId(), HttpStatus.OK);
    }

    @DeleteMapping(path="/{id}")
    public String deletePost(HttpSession session, @PathVariable("id") Long postId){
        Long loggedUserId = getLoggedUser(session);
        postService.deletePost(postId, loggedUserId);
        return BaseController.OK_MESSAGE;
    }


    @GetMapping(path="")
    @JsonView(Views.Public.class)
    public List<Post> getPosts(HttpSession session){
        getLoggedUser(session);
        return postService.getPosts();
    }

    @PutMapping(path="/{id}", consumes = "application/json")
    public String updatePost(HttpSession session,@PathVariable("id") Long postId, @Valid @RequestBody PostRequest postRequest) throws Exception {
        Long id = getLoggedUser(session);
        postService.updatePost(id, postId, postRequest.titol, postRequest.descripcio, postRequest.preu);
        return BaseController.OK_MESSAGE;
    }

    @GetMapping("/search")
    public ResponseEntity<List<PostBasicDTO>> searchPosts(@RequestParam String query) {
        List<PostBasicDTO> posts = postService.searchByTitol(query);
        return ResponseEntity.ok(posts);
    }



    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<InputStreamResource> download(@PathVariable("filename") String filename) {

        MinioClient minioClient = global.getMinioClient();
        if (minioClient == null)
            throw new ControllerException("Minio client not configured");

        try {
            InputStream file = minioClient.getObject(GetObjectArgs.builder().bucket(global.getMinioBucket()).object(filename).build());
            InputStreamResource body = new InputStreamResource(file);
            HttpHeaders headers = new HttpHeaders();
            // headers.setContentLength(body.contentLength());
            // headers.setContentDispositionFormData("attachment", "test.csv");
            headers.setContentType(MediaType.parseMediaType(URLConnection.guessContentTypeFromName(filename)));
            return ResponseEntity.ok().headers(headers).body(body);

        } catch (Exception e) {
            throw new ControllerException("Error downloading file: " + e.getMessage());
        }
    }




    private static class PostRequest {
        @NotNull
        public String titol;
        @NotNull
        public String descripcio;
        @NotNull
        public Double preu;
    }

}

