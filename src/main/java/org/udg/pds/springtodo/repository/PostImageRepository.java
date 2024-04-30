package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.PostImage;

import java.util.List;

@Component
public interface PostImageRepository extends JpaRepository<PostImage, Long> {
    PostImage findByUrl(String url);
    List<PostImage> findAllByUrl(String url);
}
