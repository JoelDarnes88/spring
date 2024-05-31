package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.udg.pds.springtodo.entity.FavoritePost;

import java.util.List;

@Repository
public interface FavoritePostRepository extends JpaRepository<FavoritePost, Long> {
    List<FavoritePost> findByUserId(Long userId);
}
