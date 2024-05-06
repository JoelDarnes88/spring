package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Message;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.User;

import java.util.List;

@Component
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserOrTargetUser(User user, User targetUser);

}
