package com.mendittzo.elastic.query.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ElasticServiceTest {

    @Autowired
    private ElasticService elasticService;

    @Test
    public void testStartIndexing() {
        elasticService.startIndexing();  // 수동으로 인덱싱 실행 되는지 여부 확인
    }
}
