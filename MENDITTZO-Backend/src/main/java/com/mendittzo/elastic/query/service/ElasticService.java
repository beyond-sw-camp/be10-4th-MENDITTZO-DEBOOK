package com.mendittzo.elastic.query.service;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ElasticService {

    // Elasticsearch와 상호작용하는 클라이언트
    private final ElasticsearchClient elasticsearchClient;

    @PostConstruct
    public void init() {
        //createBooksIndex();
        startIndexing(); // 인덱스를 생성한 후 데이터를 인덱싱
    }


    // CSV 파일을 불러와서 Elasticsearch에 인덱싱을 시작하는 메서드
    public void startIndexing() {
        try {
            // ClassPath에서 CSV 파일 경로를 찾음
            File file = new ClassPathResource("data/book.csv").getFile();
            // 인덱싱 메서드를 호출하여 CSV 파일의 데이터를 Elasticsearch에 저장
            indexCsvData(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();  // 파일을 찾지 못하거나 접근할 수 없을 때 발생하는 예외 처리
        }
    }

    // CSV 파일의 데이터를 읽어서 Elasticsearch에 인덱싱하는 메서드
    public void indexCsvData(String filePath) {
        try (Reader reader = new InputStreamReader(new FileInputStream(filePath), "EUC-KR")) {
            // CSV 파일을 파싱하여 각 레코드를 순회할 수 있게 함
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(reader);

            // 각 CSV 레코드를 ElasticDTO 객체로 변환하고 Elasticsearch에 인덱싱
            for (CSVRecord record : records) {
                ElasticDTO dto = new ElasticDTO();
                dto.setBookId(Long.parseLong(record.get("book_id")));  // book_id 값을 Long 타입으로 변환하여 설정
                dto.setTitle(record.get("title"));  // title 값을 설정
                dto.setAuthor(record.get("author"));  // author 값을 설정
                dto.setPublisher(record.get("publisher"));  // publisher 값을 설정
                // pubdate가 비어 있으면 null로 설정하고, 그렇지 않으면 그대로 설정 string으로 처리
                String pubdate = record.get("pubdate");
                dto.setPubdate(pubdate.isEmpty() ? null : pubdate);
                dto.setInfo(record.get("info").isEmpty() ? null : record.get("info"));           // 빈 값 처리
                dto.setImg(record.get("img").isEmpty() ? null : record.get("img"));

                // Elasticsearch에 인덱싱 요청 생성
                IndexRequest<ElasticDTO> indexRequest = IndexRequest.of(i -> i
                        .index("books")  // "books"라는 인덱스에 저장
                        .id(String.valueOf(dto.getBookId()))  // 고유한 book_id로 문서 ID 설정 중복 제거
                        .document(dto)   // 인덱싱할 문서 데이터로 ElasticDTO 객체를 설정
                );

                // Elasticsearch에 인덱싱 요청을 실행
                elasticsearchClient.index(indexRequest);
            }
        } catch (Exception e) {
            e.printStackTrace();  // 파일 읽기나 인덱싱 시 발생하는 예외 처리
        }
    }

    // 제목을 기준으로 검색하여 결과를 반환하는 메서드
    public List<ElasticDTO> searchByTitle(String keyword) {
        try {
            // Match 쿼리를 생성하여 title 필드에서 keyword와 일치하는 항목을 검색
            MatchQuery matchQuery = MatchQuery.of(m -> m
                    .field("title")  // 검색할 필드 이름 설정
                    .query(keyword)  // 검색할 키워드 설정
            );

            // SearchRequest 객체 생성하여 검색 요청을 설정
            SearchRequest searchRequest = new SearchRequest.Builder()
                    .index("books")  // 검색할 인덱스 이름 설정
                    .query(matchQuery._toQuery())  // 생성한 Match 쿼리를 설정
                    //.collapse(c -> c.field("book_id"))  // book_id 기준으로 중복 제거하고 싶은데 이걸 추가하면 에러가 남
                    .build();

            // Elasticsearch 클라이언트를 사용하여 검색 실행
            SearchResponse<ElasticDTO> response = elasticsearchClient.search(searchRequest, ElasticDTO.class);

            // 검색 결과를 리스트로 변환하여 반환
            return response.hits().hits().stream()
                    .map(hit -> hit.source())  // 검색 결과에서 문서 데이터를 추출
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();  // 검색 시 발생하는 예외 처리
            return List.of();  // 예외 발생 시 빈 리스트 반환
        }
    }
}
