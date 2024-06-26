package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.User;

import java.util.List;

@Component
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByNameContainingIgnoreCase(String name);

    List<User> findByUsername(@Param("username") String username);

    List<User> findByUsernameContainingIgnoreCase(String username);

    List<User> findByEmail(@Param("email") String email);
}
