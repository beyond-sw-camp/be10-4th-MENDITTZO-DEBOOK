package com.mendittzo.elastic.query.service;

import com.mendittzo.elastic.query.dto.ElasticDTO;
import com.mendittzo.elastic.query.repository.ElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElasticService {
    private final ElasticRepository elasticRepository;

    @Autowired
    public ElasticService(ElasticRepository elasticRepository) {
        this.elasticRepository = elasticRepository;
    }

    public List<ElasticDTO> searchByTitle(String keyword) {// 자동완성 및 부분 검색 기능
        return elasticRepository.findByTitleContaining(keyword);
    }
}
