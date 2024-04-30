package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.udg.pds.springtodo.entity.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT u FROM users u WHERE LOWER(u.name) LIKE LOWER(concat('%', :name, '%'))")
    List<User> findSimilarName(@Param("name") String name);

    List<User> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM users u WHERE LOWER(u.username) LIKE LOWER(concat('%', :username, '%'))")
    List<User> findSimilarUsername(@Param("username") String username);

    List<User> findByEmail(@Param("email") String email);
}
