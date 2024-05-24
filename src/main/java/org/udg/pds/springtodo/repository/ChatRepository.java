package org.udg.pds.springtodo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.udg.pds.springtodo.entity.Chat;
import org.udg.pds.springtodo.entity.Message;
import org.udg.pds.springtodo.entity.Post;
import org.udg.pds.springtodo.entity.User;

import java.util.List;
import java.util.Optional;

@Component
public interface ChatRepository extends JpaRepository<Chat, Long> {
    List<Chat> findByUserOrTargetUser(User user, User targetUser);

    Optional<Chat> findByUserAndTargetUserAndPost(User user, User targetUser, Post post);
    Optional<Chat> findByTargetUserAndUserAndPost(User targetUser, User user, Post post);

}
