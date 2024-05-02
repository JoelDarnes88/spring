package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.Token;

import java.util.List;

@Component
public interface TokenRepository extends JpaRepository<Token, Long> {

    List<Token> findByToken(@Param("token") String token);
}
