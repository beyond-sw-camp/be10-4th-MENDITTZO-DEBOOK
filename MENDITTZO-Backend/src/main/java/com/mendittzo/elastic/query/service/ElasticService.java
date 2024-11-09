package com.mendittzo.elastic.query.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.mendittzo.elastic.query.dto.ElasticDTO;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import java.io.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticService {

    private final ElasticsearchClient elasticsearchClient;
    private final ElasticsearchOperations elasticsearchOperations;

    // 1. CSV 파일 불러오기 및 인덱싱 "data/books.csv" 위치에 있는 CSV 파일을 불러와 인덱싱 시작
    public void startIndexing() {
        try {
            File file = new ClassPathResource("data/books.csv").getFile(); // 파일 경로 설정
            indexCsvData(file.getAbsolutePath());  // 인덱싱 시작
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 2. CSV 파일의 데이터를 인덱싱 CSV 파일의 각 행을 ElasticDTO 객체로 변환하여 Elasticsearch에 인덱싱 맥에서도 인덱싱 되게 예외추가를 곁들인
    public void indexCsvData(String filePath) {
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "EUC-KR")) {
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .parse(reader);

            for (CSVRecord record : records) {
                ElasticDTO DTO = new ElasticDTO();
                DTO.setBookId(Long.parseLong(record.get("book_id")));
                DTO.setTitle(record.get("title"));
                DTO.setAuthor(record.get("author"));
                DTO.setPublisher(record.get("publisher"));
                DTO.setPubdate(record.get("pubdate"));
                DTO.setInfo(record.get("info"));
                DTO.setImg(record.get("img"));

                // Elasticsearch에 인덱싱 요청
                IndexRequest<ElasticDTO> indexRequest = IndexRequest.of(i -> i
                        .index("books")  // 인덱스 이름 설정
                        .document(DTO)   // 문서 데이터 설정
                );
                elasticsearchClient.index(indexRequest);  // 인덱싱 요청 실행
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 3. 제목을 기준으로 검색 처리 (자동완성 및 부분 검색) title 필드를 기준으로 검색하여 일치하는 결과 반환
    public List<ElasticDTO> searchByTitle(String keyword) {
        try {
            //Match 쿼리 생성
            Query matchQuery = (Query) MatchQuery.of(m->m
                    .field("title")
                    .query(keyword)
            )._toQuery();
            // 서치 리케스트 생성
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("books")
                    .query((co.elastic.clients.elasticsearch._types.query_dsl.Query) matchQuery)
                    .build();
            // 검색 요청 및 결과 처리
            SearchResponse<ElasticDTO> response = elasticsearchClient.search(searchRequest, ElasticDTO.class);
            return response.hits().hits().stream()
                    .map(hit -> hit.source())
                    .collect(Collectors.toList());
        }catch (Exception e){
            e.printStackTrace();
            return List.of();
        }
    }
}
