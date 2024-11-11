package com.mendittzo.elastic.query.service;

import co.elastic.clients.elasticsearch._types.analysis.Analyzer;
import co.elastic.clients.elasticsearch._types.analysis.CustomAnalyzer;
import co.elastic.clients.elasticsearch._types.mapping.Property;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchPhraseQuery;
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchClient elasticsearchClient;
    private static final int BATCH_SIZE = 500; // 배치 단위 크기 설정

    @PostConstruct
    public void init() {
        createBooksIndex();
        initializeIndexing(); // 비동기 호출 없이 인덱싱 초기화
    }

    // Nori 분석기를 적용하여 books 인덱스를 생성하는 메서드
    public void createBooksIndex() {
        try {
            boolean indexExists = elasticsearchClient.indices().exists(e -> e.index("books")).value();

            if (!indexExists) {
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

    // startIndexing 메서드를 비동기 호출로 실행
    public void initializeIndexing() {
        startIndexing();
    }

    @Async
    public void startIndexing() {
        try {
            File file = new ClassPathResource("data/book.csv").getFile();
            indexCsvData(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // CSV 데이터를 배치 단위로 인덱싱
    public void indexCsvData(String filePath) {
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "EUC-KR")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);
            List<IndexRequest<ElasticDTO>> batchRequests = new ArrayList<>();

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

                batchRequests.add(indexRequest);

                // BATCH_SIZE 만큼 수집된 경우 Elasticsearch에 전송
                if (batchRequests.size() >= BATCH_SIZE) {
                    sendBatchRequests(batchRequests);
                    batchRequests.clear(); // 전송 후 리스트 비우기
                }
            }

            // 남아있는 레코드 전송
            if (!batchRequests.isEmpty()) {
                sendBatchRequests(batchRequests);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 배치 단위로 Elasticsearch에 전송
    private void sendBatchRequests(List<IndexRequest<ElasticDTO>> requests) {
        try {
            BulkRequest.Builder bulkBuilder = new BulkRequest.Builder();

            // Convert each IndexRequest to a BulkOperation and add to the BulkRequest
            List<BulkOperation> bulkOperations = requests.stream()
                    .map(request -> BulkOperation.of(b -> b
                            .index(i -> i
                                    .index(request.index())
                                    .id(request.id())
                                    .document(request.document())
                            )
                    ))
                    .collect(Collectors.toList());

            // Add all BulkOperations to the bulk request
            bulkBuilder.operations(bulkOperations);

            // Execute the bulk request
            BulkResponse bulkResponse = elasticsearchClient.bulk(bulkBuilder.build());

            if (bulkResponse.errors()) {
                System.out.println("Some errors occurred during batch indexing.");
            } else {
                System.out.println("Batch indexed successfully.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<ElasticDTO> searchByTitle(String keyword) {
        try {
            MatchPhraseQuery matchPhraseQuery = MatchPhraseQuery.of(m -> m.field("title").query(keyword));
            PrefixQuery prefixQuery = PrefixQuery.of(m -> m.field("title").value(keyword));
            MatchQuery matchQuery = MatchQuery.of(m -> m.field("title").query(keyword));

            BoolQuery boolQuery = BoolQuery.of(b -> b
                    .should(matchPhraseQuery._toQuery())
                    .should(prefixQuery._toQuery())
                    .should(matchQuery._toQuery())
                    .minimumShouldMatch("1")
            );

            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("books")
                    .query(boolQuery._toQuery())
                    .build();

            SearchResponse<ElasticDTO> response = elasticsearchClient.search(searchRequest, ElasticDTO.class);

            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
