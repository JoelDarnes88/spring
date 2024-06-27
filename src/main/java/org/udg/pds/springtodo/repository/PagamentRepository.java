package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Pagament;
import org.udg.pds.springtodo.entity.User;

import java.util.List;

@Component
public interface PagamentRepository extends JpaRepository<Pagament, Long> {
    List<Pagament> findAllByUser(User user);
    boolean existsByChat(Chat chat);
}
