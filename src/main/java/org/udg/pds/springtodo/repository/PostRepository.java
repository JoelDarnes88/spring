package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Post;

import java.util.List;

@Component
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitolContainingIgnoreCase(String titol);
}
