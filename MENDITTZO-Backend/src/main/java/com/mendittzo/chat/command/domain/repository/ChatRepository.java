package com.mendittzo.chat.command.domain.repository;

import com.mendittzo.chat.command.domain.aggregate.Chat;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface ChatRepository extends CrudRepository<Chat, String> {

    List<Chat> findByChatroomId(Long chatroomId);
}
