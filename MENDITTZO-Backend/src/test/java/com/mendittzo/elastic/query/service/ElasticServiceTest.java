package com.mendittzo.elastic.query.service;

import com.mendittzo.elastic.query.dto.ElasticDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ElasticServiceTest {

    @Autowired
    private ElasticService elasticService;

    @BeforeEach
    public void setup() {
        // 테스트 환경에서 인덱스 생성을 준비합니다.
        elasticService.createBooksIndex();
    }

    // 인덱스 생성 테스트
    @Test
    public void testCreateBooksIndex() {
        elasticService.createBooksIndex();
        System.out.println("createBooksIndex 테스트 완료");
    }

    // 비동기 인덱싱 실행 테스트
    @Test
    public void testStartIndexing() {
        elasticService.startIndexing();
        System.out.println("startIndexing 테스트 완료");
    }

    // CSV 파일 데이터를 배치 단위로 인덱싱하는 메서드 테스트
    @Test
    public void testIndexCsvData() {
        String filePath = "src/main/resources/data/book.csv"; // 테스트 CSV 파일 경로
        elasticService.indexCsvData(filePath);
        System.out.println("indexCsvData 테스트 완료");
    }

    // 단일 배치 전송 메서드 테스트
    @Test
    public void testSendBatchRequests() {
        ElasticDTO dto = new ElasticDTO();
        dto.setBookId(12345L);
        dto.setTitle("Test Book");
        dto.setAuthor("Test Author");
        dto.setPublisher("Test Publisher");
        dto.setPubdate("2024-11-11");
        dto.setInfo("Test Info");
        dto.setImg("test-img-url");

        List.of(dto).forEach(request -> elasticService.indexCsvData("src/main/resources/data/book.csv"));
        System.out.println("sendBatchRequests 테스트 완료");
    }

    // 제목으로 검색 테스트
    @Test
    public void testSearchByTitle() {
        String keyword = "지리산";
        List<ElasticDTO> results = elasticService.searchByTitle(keyword);
        assertFalse(results.isEmpty(), "검색 결과가 비어 있지 않아야 합니다.");
        results.forEach(result -> System.out.println(result.getTitle()));
        System.out.println("searchByTitle 테스트 완료");
    }
}
