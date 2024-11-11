package com.mendittzo.recommend.command.domain.repository;

import com.mendittzo.recommend.command.domain.aggregate.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatHistoryRepository extends MongoRepository<ChatHistory, String>{

    // 사용자 번호를 기준으로 챗 기록을 찾는 쿼리
    @Query("{ 'user_id' : ?0 }")
    List<ChatHistory> findByUserId(Long userId);
}
