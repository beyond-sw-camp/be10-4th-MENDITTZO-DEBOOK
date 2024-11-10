package com.mendittzo.elastic.query.service;

import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.analysis.CustomAnalyzer;
import co.elastic.clients.elasticsearch._types.analysis.NoriTokenizer;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.PrefixQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import com.mendittzo.elastic.query.dto.ElasticDTO;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void init() {
        createBooksIndex();
        startIndexing();
    }

    // Nori 분석기를 적용하여 books 인덱스를 생성하는 메서드
    public void createBooksIndex() {
        try {
            // 인덱스가 이미 존재하는지 확인
            boolean indexExists = elasticsearchClient.indices().exists(e -> e.index("books")).value();

            if (!indexExists) {
                // 인덱스가 존재하지 않을 경우에만 생성
                CreateIndexRequest request = new CreateIndexRequest.Builder()
                        .index("books")
                        .settings(s -> s
                                .analysis(a -> a
                                        .analyzer("korean_analyzer", Analyzer.of(an -> an
                                                .custom(CustomAnalyzer.of(ca -> ca
                                                        .tokenizer("nori_tokenizer")
                                                ))
                                        ))
                                )
                        )
                        .mappings(m -> m
                                .properties("book_id", Property.of(p -> p.keyword(k -> k)))
                                .properties("title", Property.of(p -> p.text(t -> t.analyzer("korean_analyzer"))))
                        )
                        .build();

                CreateIndexResponse response = elasticsearchClient.indices().create(request);

                if (response.acknowledged()) {
                    System.out.println("Index created successfully.");
                } else {
                    System.out.println("Index creation not acknowledged.");
                }
            } else {
                System.out.println("Index already exists, skipping creation.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void startIndexing() {
        try {
            File file = new ClassPathResource("data/book.csv").getFile();
            indexCsvData(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void indexCsvData(String filePath) {
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "EUC-KR")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            for (CSVRecord record : records) {
                ElasticDTO dto = new ElasticDTO();
                dto.setBookId(Long.parseLong(record.get("book_id")));
                dto.setTitle(record.get("title"));
                dto.setAuthor(record.get("author"));
                dto.setPublisher(record.get("publisher"));
                String pubdate = record.get("pubdate");
                dto.setPubdate(pubdate.isEmpty() ? null : pubdate);
                dto.setInfo(record.get("info").isEmpty() ? null : record.get("info"));
                dto.setImg(record.get("img").isEmpty() ? null : record.get("img"));

                IndexRequest<ElasticDTO> indexRequest = IndexRequest.of(i -> i
                        .index("books")
                        .id(String.valueOf(dto.getBookId()))
                        .document(dto)
                );

                elasticsearchClient.index(indexRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<ElasticDTO> searchByTitle(String keyword) {
        try {
            // Prefix 쿼리를 생성하여 title 필드에서 keyword로 시작하는 항목을 검색
            PrefixQuery prefixQuery = PrefixQuery.of(m -> m
                    .field("title")
                    .value(keyword)
            );

            // SearchRequest 객체 생성하여 검색 요청을 설정
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("books")
                    .query(prefixQuery._toQuery())
                    .build();

            // Elasticsearch 클라이언트를 사용하여 검색 실행
            SearchResponse<ElasticDTO> response = elasticsearchClient.search(searchRequest, ElasticDTO.class);

            // 검색 결과를 리스트로 변환하여 반환
            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

}
