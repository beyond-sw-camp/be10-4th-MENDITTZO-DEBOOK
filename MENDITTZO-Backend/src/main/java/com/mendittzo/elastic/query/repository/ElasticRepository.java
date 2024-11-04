package com.mendittzo.elastic.query.repository;

import com.mendittzo.elastic.query.dto.ElasticDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ElasticRepository extends ElasticsearchRepository<ElasticDTO, Long> {
    List<ElasticDTO> findByTitleContaining(String title); // 자동완성 및 부분 검색을 위한 메서드
}
