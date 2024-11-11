package com.mendittzo.chat.command.domain.repository;

import com.mendittzo.chat.command.domain.aggregate.Chatroom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long> {
}
